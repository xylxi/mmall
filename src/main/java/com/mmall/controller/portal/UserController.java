package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do")
    public ServerResponse<User> login(String username, String password, HttpSession session) {

        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping("logout.do")
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出成功");
    }

    @RequestMapping("register.do")
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @RequestMapping("check_valid.do")
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    @RequestMapping("get_user_info.do")
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取该用户的信息");
        }
        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping("forget_get_question")
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    @RequestMapping("forget_check_answer")
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.forgetCheckAnswer(username, question, answer);
    }

    @RequestMapping("forget_reset_password.do")
    public ServerResponse<String> forgetResertPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResertPassword(username, passwordNew, forgetToken);
    }

    @RequestMapping("reset_password.do")
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取该用户的信息");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    @RequestMapping("update_information.do")
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取该用户的信息");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping("get_information.do")
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iUserService.getUserInfomation(currentUser.getId());
    }

}
