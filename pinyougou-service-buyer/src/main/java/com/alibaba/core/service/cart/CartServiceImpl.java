package com.alibaba.core.service.cart;

import com.alibaba.core.dao.item.ItemDao;
import com.alibaba.core.dao.seller.SellerDao;
import com.alibaba.core.pojo.cart.Cart;
import com.alibaba.core.pojo.item.Item;
import com.alibaba.core.pojo.order.OrderItem;
import com.alibaba.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

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
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


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

    /**
     * 将本地购物车合并到redis中去
     *
     * @param username
     * @param newCartList
     */
    @Override
    public void mergeCartList(String username, List<Cart> newCartList) {
        //从redis取出老车
        List<Cart> oldCartList = (List<Cart>) redisTemplate.boundHashOps("BUYER_CART").get(username);
        //将新车合并到老车
        oldCartList = newCartListMergeOldCartList(newCartList, oldCartList);
        //将老车保存到redis中去
        redisTemplate.boundHashOps("BUYER_CART").put(username, oldCartList);
    }

    /**
     * 从redis取出购物车
     *
     * @param name
     * @return
     */
    @Override
    public List<Cart> findCartListFromRedis(String name) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("BUYER_CART").get(name);
        return cartList;
    }

    //将新车合并到老车
    private List<Cart> newCartListMergeOldCartList(List<Cart> newCartList, List<Cart> oldCartList) {
        if (newCartList != null) {
            if (oldCartList != null) {
                //新老车都不为空
                //判断是否属于同一个商家
                for (Cart newCart : newCartList) {//遍历新车 购物项
                    int sellerIndexOf = oldCartList.indexOf(newCart); //判断老车 是否包含 新车 购物项  有(索引) 无(-1)
                    if (sellerIndexOf != -1) {
                        //属于同一商家 判断是否属于同一商品
                        List<OrderItem> newCartOrderItemList = newCart.getOrderItemList(); //新车购物项中的商品
                        List<OrderItem> oldCartOrderItemList = oldCartList.get(sellerIndexOf).getOrderItemList();//老车购物项中的商品
                        for (OrderItem newOrderItem : newCartOrderItemList) {
                            int itemIndexOf = oldCartOrderItemList.indexOf(newOrderItem);
                            if (itemIndexOf != -1) {
                                //是同款商品 合并数量
                                OrderItem oldOrderItem = oldCartOrderItemList.get(itemIndexOf);//老车购物项
                                oldOrderItem.setNum(oldOrderItem.getNum() + newOrderItem.getNum()); //合并数量

                            } else {
                                //不是同款商品 ,直接加入到该商家的购物项中
                                oldCartOrderItemList.add(newOrderItem);
                            }
                        }
                    } else {
                        //不属于同一商家  直接装车
                        oldCartList.add(newCart);
                    }
                }
            } else {
                //老车为空 返回新车
                return newCartList;
            }
        } else {
            //新车为空 返回老车
            return oldCartList;
        }
        return oldCartList;
    }
}
