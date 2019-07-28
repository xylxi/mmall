package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    @RequestMapping("add.do")
    public ServerResponse add(HttpSession session, Shipping shipping) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return shippingService.add(currentUser.getId(), shipping);
    }

    @RequestMapping("del.do")
    public ServerResponse del(HttpSession session, Integer shippingId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return shippingService.del(currentUser.getId(), shippingId);
    }

    @RequestMapping("update.do")
    public ServerResponse update(HttpSession session, Shipping shipping) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return shippingService.update(currentUser.getId(), shipping);
    }

    @RequestMapping("select.do")
    public ServerResponse select(HttpSession session, Integer shippingId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return shippingService.select(currentUser.getId(), shippingId);
    }

    @RequestMapping("list.do")
    public ServerResponse<PageInfo> list(
            HttpSession session,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return shippingService.list(currentUser.getId(), pageNum, pageSize);
    }

}
