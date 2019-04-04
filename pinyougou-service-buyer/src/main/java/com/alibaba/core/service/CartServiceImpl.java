package com.alibaba.core.service;

import com.alibaba.core.dao.item.ItemDao;
import com.alibaba.core.dao.seller.SellerDao;
import com.alibaba.core.pojo.cart.Cart;
import com.alibaba.core.pojo.item.Item;
import com.alibaba.core.pojo.order.OrderItem;
import com.alibaba.core.pojo.seller.Seller;
import com.alibaba.core.service.cart.CartService;
import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName CartServiceImpl
 * @Description 购物车实现
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 15:50 2019/4/3
 * @Version 2.1
 **/
@Service
public class CartServiceImpl implements CartService {

    @Resource
    private ItemDao itemDao;
    @Resource
    private SellerDao sellerDao;


    /**
     * 获取商家id
     *
     * @param id
     * @return
     */
    @Override
    public Item findOne(Long id) {
        return itemDao.selectByPrimaryKey(id);
    }

    /**
     * 填充购物车中的数据
     *
     * @param cartList
     * @return
     */
    @Override
    public List<Cart> autoDataToCart(List<Cart> cartList) {
        for (Cart cart : cartList) {
            //填充店铺名称
            Seller seller = sellerDao.selectByPrimaryKey(cart.getSellerId());
            cart.setSellerName(seller.getNickName());

            //填充购物项数据
            List<OrderItem> orderItemList = cart.getOrderItemList();
            if (orderItemList != null && orderItemList.size() > 0) {
                for (OrderItem orderItem : orderItemList) {
                    Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                    orderItem.setPicPath(item.getImage());      //填充商品图片
                    orderItem.setTitle(item.getTitle());        //填充商品标题
                    orderItem.setPrice(item.getPrice());        //填充商品单价

                    BigDecimal bigDecimal = new BigDecimal(item.getPrice().doubleValue() * orderItem.getNum());
                    orderItem.setTotalFee(bigDecimal);          //填充商品小计

                }
            }
        }
        return cartList;
    }

}
