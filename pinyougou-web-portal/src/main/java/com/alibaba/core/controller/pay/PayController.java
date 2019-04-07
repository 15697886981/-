package com.alibaba.core.controller.pay;

import com.alibaba.core.entity.Result;
import com.alibaba.core.service.pay.PayService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName PayController
 * @Description
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 16:12 2019/4/6
 * @Version 2.1
 **/
@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private PayService payService;

    @RequestMapping("/createNative.do")
    public Map<String, String> createNative() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative(username);
    }

    @RequestMapping("/queryPayStatus.do")
    public Result queryPayStatus(String out_trade_no) {
        int time = 0;
        try {
            while (true) {
                Map<String, String> map = payService.queryPayStatus(out_trade_no);
                String trade_state = map.get("trade_state");
                if ("SUCCESS".equals(trade_state)) {
                    return new Result(true, "支付成功");
                } else {
                    Thread.sleep(5000);//睡5秒
                    time++;
                }
                if (time > 360) {
                    return new Result(true, "二维码超时");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "error");
        }
    }
}
