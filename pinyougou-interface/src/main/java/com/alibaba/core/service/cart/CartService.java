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

    /**
     * 将本地购物车合并到redis中去
     *
     * @param username
     * @param cartList
     */
    void mergeCartList(String username, List<Cart> cartList);


    /**
     * 从redis取出购物车
     *
     * @param name
     * @return
     */
    List<Cart> findCartListFromRedis(String name);
}
