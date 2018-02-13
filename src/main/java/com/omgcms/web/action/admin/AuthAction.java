package com.omgcms.web.action.admin;

import com.omgcms.bean.RestResponse;
import com.omgcms.exception.CmsExceptionConstants;
import com.omgcms.exception.CmsRuntimeException;
import com.omgcms.model.core.User;
import com.omgcms.service.RealmService;
import com.omgcms.service.UserService;
import com.omgcms.util.RestResponseGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthAction {

    private static final Logger _log = LoggerFactory.getLogger(AuthAction.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RealmService realmService;

    @RequestMapping("/checkLogin")
    public RestResponse<?> checkLogin(@RequestParam(value = "oc_account", required = false) String account,
                                      @RequestParam(value = "oc_password", required = false) String password,
                                      @RequestParam(value = "oc_rememberme", required = false) boolean rememberme) {

        UsernamePasswordToken token = null;

        try {

            if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
                throw new CmsRuntimeException(CmsExceptionConstants.LOGIN_ERROR_EMPTY_ACCOUNT_PWD);
            }

            Subject currentSubject = SecurityUtils.getSubject();

            if (currentSubject.isAuthenticated()) {
                User user = (User) currentSubject.getPrincipal();
                if (user.getUserAccount().equals(account)) {

                    _log.debug("User {} have already logged in!", account);

                    return RestResponseGenerator.genResult(RestResponse.STATUS_SUCCESS, null, "You have already logged in!");
                } else {

                    currentSubject.logout();
                    realmService.clearShiroRealmCache();
                }

            }

            token = new UsernamePasswordToken(account, password);

            if (rememberme) {
                token.setRememberMe(true);
            }

            currentSubject.login(token);
            User user = (User) SecurityUtils.getSubject().getPrincipal();

            if (user != null) {
                user.setLastLoginDate(new Date());
                userService.saveAndFlush(user);
            }

            return RestResponseGenerator.genSuccessResult();

        } catch (Exception e) {

            _log.error("Login failed:" + e);

            String errorCode = CmsExceptionConstants.ERROR_UNKNOWN;

            if (e instanceof IncorrectCredentialsException) {
                errorCode = CmsExceptionConstants.LOGIN_ERROR_INVALID_PASSWORD_ACCOUNT;
            } else if (e instanceof UnknownAccountException) {
                errorCode = CmsExceptionConstants.LOGIN_ERROR_INVALID_PASSWORD_ACCOUNT;
            } else if (e instanceof CmsRuntimeException) {
                errorCode = ((CmsRuntimeException) e).getErrorCode();
            }

            if (token != null) {
                token.clear();
            }

            throw new CmsRuntimeException(errorCode);

        }

    }


    @RequestMapping("/logout")
    public RestResponse<?> logout() {

        Subject currentSubject = SecurityUtils.getSubject();

        if (currentSubject.isAuthenticated()) {

            realmService.clearShiroRealmCache();
            currentSubject.logout();

            return RestResponseGenerator.genSuccessResult();
        } else {

            return RestResponseGenerator.genErrorResult("You have not sign in!");
        }

    }

}
