package com.alibaba.core.controller.order;

import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.order.Order;
import com.alibaba.core.service.order.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName OrderController
 * @Description
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 23:20 2019/4/5
 * @Version 2.1
 **/
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @RequestMapping("/add.do")
    public Result add(@RequestBody Order order) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            orderService.add(username, order);
            return new Result(true, "下单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "下单失败");
        }
    }

}
