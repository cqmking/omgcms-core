package com.omgcms.exception;

import com.omgcms.bean.RestResponse;
import com.omgcms.util.CmsUtil;
import com.omgcms.util.ErrorCode;
import com.omgcms.util.RestResponseGenerator;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = {RestController.class})
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler
    @ResponseBody
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> RestResponse<T> runtimeExceptionHandler(Exception e) {

        logger.error("---------> huge error!");

        if (e instanceof CmsRuntimeException) {

            CmsRuntimeException cre = (CmsRuntimeException) e;
            return RestResponseGenerator.genErrorResult(CmsUtil.getLocaleMessage(cre.getErrorCode()));

        } else if (e instanceof CmsException) {

            CmsException ce = (CmsException) e;
            return RestResponseGenerator.genErrorResult(CmsUtil.getLocaleMessage(ce.getErrorCode()));

        } else {
            e.printStackTrace();
            return RestResponseGenerator.genErrorResult(e.getMessage());
        }

    }

    @ExceptionHandler({MethodArgumentNotValidException.class, UnauthenticatedException.class,
            AuthenticationException.class, UnauthorizedException.class, AuthorizationException.class})
    @ResponseBody
    public <T> RestResponse<T> illegalParamsExceptionHandler(MethodArgumentNotValidException e) {
        logger.error("---------> invalid request!", e);
        return RestResponseGenerator.genErrorResult(ErrorCode.ILLEGAL_PARAMS);
    }


}
