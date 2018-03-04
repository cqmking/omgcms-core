package com.omgcms.web.action.admin;

import com.omgcms.bean.RestResponse;
import com.omgcms.model.core.User;
import com.omgcms.service.UserService;
import com.omgcms.util.RestResponseGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserAction {

//    private static final Logger _log = LoggerFactory.getLogger(UserAction.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public RestResponse<?> getUserList(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {

        Page<User> userListPage = userService.findUsers(pageNo, pageSize, "userId", null);

        return RestResponseGenerator.genSuccessResult(userListPage);
    }

}
