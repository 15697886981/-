package com.alibaba.core.service.cart;

import com.alibaba.core.pojo.cart.Cart;
import com.alibaba.core.pojo.item.Item;

import java.util.List;

public interface CartService {
    /**
     * 获取商家id
     *
     * @param id
     * @return
     */
    Item findOne(Long id);

    /**
     * 填充购物车中的数据
     *
     * @param cartList
     * @return
     */
    List<Cart> autoDataToCart(List<Cart> cartList);
}
