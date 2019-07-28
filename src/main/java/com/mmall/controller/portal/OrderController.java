package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("create.do")
    public ServerResponse create(HttpSession session, Integer shippingId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iOrderService.createOrder(currentUser.getId(), shippingId);
    }

    @RequestMapping("cancel.do")
    public ServerResponse cancel(HttpSession session, Long orderNo) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        return iOrderService.cancel(currentUser.getId(), orderNo);
    }

    @RequestMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping("detail.do")
    public ServerResponse detail(HttpSession session, Long orderNo) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }

    @RequestMapping("list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }

    @RequestMapping("pay.do")
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        String path = session.getServletContext().getRealPath("upload");
        return iOrderService.pay(currentUser.getId(), orderNo, path);
    }

    @RequestMapping("alipay_callback.do")
    public Object alipayCallback(HttpServletRequest request) {

        Map<String, String> params = Maps.newHashMap();

        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valuesStr = "";
            for ( int i = 0 ; i < values.length ; i++) {
                valuesStr = (i == values.length - 1)
                        ? valuesStr + values[i]
                        : valuesStr + values[i] + ",";
            }
            params.put(name, valuesStr);
        }

        log.info("支付宝回调, sign={}, trade_status:{}, 参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        // 验证回调的正确性，是否是支付宝发的已经避免重复通知
        params.remove("sign_type");

        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerResponse.createBySuccessMessage("非法请求，验证不通过");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常", e);
        }

        // todo 验证各种数据正确性

        // 回调支付宝接口验证
        ServerResponse serverResponse = iOrderService.alipayCallback(params);
        if (serverResponse.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;

    }

    @RequestMapping("query_order_pay_status.do")
    public ServerResponse queryOrderPayStatus(HttpSession session, Long orderNo) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ResponseCode needLoginCode =  ResponseCode.NEED_LOGIN;
            return ServerResponse.createByErrorCodeMessage(needLoginCode.getCode(), needLoginCode.getDesc());
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(currentUser.getId(), orderNo);
        return ServerResponse.createBySuccess(serverResponse.isSuccess());
    }

}
