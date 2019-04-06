package com.alibaba.core.service.order;

import com.alibaba.core.dao.item.ItemDao;
import com.alibaba.core.dao.order.OrderDao;
import com.alibaba.core.dao.order.OrderItemDao;
import com.alibaba.core.pojo.cart.Cart;
import com.alibaba.core.pojo.item.Item;
import com.alibaba.core.pojo.order.Order;
import com.alibaba.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.utils.core.uniquekey.IdWorker;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderServiceImpl
 * @Description 订单操作
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 21:11 2019/4/5
 * @Version 2.1
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private OrderItemDao orderItemDao;
    @Resource
    private IdWorker idWorker;
    @Resource
    private ItemDao itemDao;

    /**
     * 提交订单
     *
     * @param username
     * @param order
     */
    @Transactional
    @Override
    public void add(String username, Order order) {

        //保存订单 根据商家id合并
        List<Cart> orderList = (List<Cart>) redisTemplate.boundHashOps("BUYER_CART").get(username);

        //获取商家id
        if (orderList != null && orderList.size() > 0) {
            for (Cart cart : orderList) {//根据商家id进行分类
                //订单明细
                long orderId = idWorker.nextId();
                order.setOrderId(orderId);// 主键
                double payment = 0f;// 订单的总金额（购买的该商家下的商品的总金额）
                order.setPaymentType("1");// 支付方式：在线支付
                order.setStatus("1");// 订单状态：待付款
                order.setCreateTime(new Date());// 订单创建日期
                order.setUserId(username);// 订单用户
                order.setSellerId(cart.getSellerId());// 商家id


                //保存订单明细
                List<OrderItem> orderItemList = cart.getOrderItemList();
                if (orderItemList != null && orderItemList.size() > 0) {
                    for (OrderItem orderItem : orderItemList) {
                        // 订单明细
                        long id = idWorker.nextId();
                        orderItem.setId(id);    // 订单明细的主键
                        Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                        orderItem.setGoodsId(item.getGoodsId());    // spu的id
                        orderItem.setOrderId(orderId);  // 订单id
                        orderItem.setTitle(item.getTitle());    // 商品标题
                        orderItem.setPrice(item.getPrice());    // 单价
                        // 订单明细的小计
                        double totalFee = item.getPrice().doubleValue() * orderItem.getNum();
                        payment += totalFee;
                        orderItem.setTotalFee(new BigDecimal(totalFee));   // 明细的小计
                        orderItem.setPicPath(item.getImage());  // 图片
                        orderItem.setSellerId(item.getSellerId());  // 商家id

                        orderItemDao.insertSelective(orderItem);

                    }

                }
                //总金额 = 该商家的订单明细价格
                order.setPayment(new BigDecimal(payment));
                orderDao.insertSelective(order);
            }
            //清空购物车
            redisTemplate.boundHashOps("BUYER_CART").delete(username);
        }
    }
}
