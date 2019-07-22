package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController()
@RequestMapping("/m/user")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping("login.do")
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, response.getData());
                return response;
            }else {
                return ServerResponse.createByErrorMessage("不是管理员，无法登录");
            }
        }
        return response;
    }

}
