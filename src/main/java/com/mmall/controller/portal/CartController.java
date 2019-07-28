package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    public ServerResponse<CartVo> list(HttpSession session, Integer count, Integer productId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iCartService.list(currentUser.getId());
    }

    @RequestMapping("add.do")
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iCartService.add(currentUser.getId(), productId, count);
    }

    @RequestMapping("update.do")
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iCartService.update(currentUser.getId(), productId, count);
    }

    @RequestMapping("delete.do")
    public ServerResponse<CartVo> deleteProduct(HttpSession session, String productIds) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iCartService.deleteProduct(currentUser.getId(), productIds);
    }

    @RequestMapping("select_all.do")
    public ServerResponse<CartVo> selectAll(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iCartService.selectOrUnselect(currentUser.getId(), Const.Cart.CHECKED, null);
    }

    @RequestMapping("un_select_all.do")
    public ServerResponse<CartVo> UnSelectAll(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iCartService.selectOrUnselect(currentUser.getId(), Const.Cart.UNCHECKED, null);
    }

    @RequestMapping("select.do")
    public ServerResponse<CartVo> select(HttpSession session, Integer productId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iCartService.selectOrUnselect(currentUser.getId(), Const.Cart.CHECKED, productId);
    }

    @RequestMapping("un_select.do")
    public ServerResponse<CartVo> UnSelect(HttpSession session, Integer productId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iCartService.selectOrUnselect(currentUser.getId(), Const.Cart.UNCHECKED, productId);
    }

    @RequestMapping("get_cart_product_count.do")
    public ServerResponse<Integer> getCartProductCount(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCarProductCount(currentUser.getId());
    }

}
