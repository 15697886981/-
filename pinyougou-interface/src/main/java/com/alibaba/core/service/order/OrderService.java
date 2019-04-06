package com.alibaba.core.service.order;

import com.alibaba.core.pojo.order.Order;

/**
 * @ClassName OrderService
 * @Description 订单操作
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 21:10 2019/4/5
 * @Version 2.1
 **/

public interface OrderService {
    /**
     * 提交订单
     *
     * @param username
     * @param order
     */
    void add(String username, Order order);

}
