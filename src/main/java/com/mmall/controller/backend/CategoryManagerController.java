package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/m/category")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    public ServerResponse addCategory(HttpSession session,
                                      String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.addCategory(categoryName, parentId);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("set_category_name.do")
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("get_category.do")
    public ServerResponse<List<Category>> getChildrenParallelCategory(HttpSession session,
                                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("get_deep_category.do")
    public ServerResponse<List<Category>> getCategoryAndDeepChildrenCategory(HttpSession session,
                                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 查询当前节点和所有递归子节点的品类
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

}
