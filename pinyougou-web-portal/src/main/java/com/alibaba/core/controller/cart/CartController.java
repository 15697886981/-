package com.alibaba.core.controller.cart;

import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.cart.Cart;
import com.alibaba.core.pojo.item.Item;
import com.alibaba.core.pojo.order.OrderItem;
import com.alibaba.core.service.cart.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CartController
 * @Description 购物车
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 17:22 2019/4/2
 * @Version 2.1
 **/
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    //服务器端支持CORS
    //response.setHeader("Access-Control-Allow-Origin", "http://localhost:9003");
    //携带cookie
    //response.setHeader("Access-Control-Allow-Credentials", "true");
    @RequestMapping("/addGoodsToCartList.do")
    @CrossOrigin(origins = {"http://localhost:9003"})
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletRequest request,
                                     HttpServletResponse response) {
        try {
            // 将商品加入购物车具体的业务实现
            //1.定义一个空车的集合
            List<Cart> cartList = null;
            //1.判断本地是否有车子
            Boolean flag = false;//定义一个开关
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                //有车子
                for (Cookie cookie : cookies) {
                    //cookie 的数据  key-value(json串)
                    if ("BUYER_CART".equals(cookie.getName())) {
                        //有 :从cookie中取出来
                        String value = cookie.getValue();
                        String decode = URLDecoder.decode(value, "UTF-8");
                        cartList = JSON.parseArray(decode, Cart.class);
                        flag = true;
                    }
                }
            }
            //没车
            if (cartList == null) {
                cartList = new ArrayList<>();
            }
            //填充数据
            Cart cart = new Cart();
            Item item = cartService.findOne(itemId);//itemId 库存id
            cart.setSellerId(item.getSellerId()); //商家id
            List<OrderItem> orderItemList = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(itemId);
            orderItem.setNum(num);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);  //购物项  库存id 以及购买数量
            //将商品进行装车
            //判断是否属于同一个商家
            int sellerIndexOf = cartList.indexOf(cart);
            if (sellerIndexOf != -1) {
                //属于同一个商家------>继续判断是否属于同一款商品
                //取出之前  购物项的数据
                List<OrderItem> oldOrderItemList = cartList.get(sellerIndexOf).getOrderItemList();
                //判断本次的购物项在之前是否存在
                int indexOf = oldOrderItemList.indexOf(orderItem);
                if (indexOf != -1) {
                    //同款商品 合并数量
                    OrderItem oldOrderItem = oldOrderItemList.get(indexOf);
                    oldOrderItem.setNum(oldOrderItem.getNum() + num);
                } else {
                    //同商家 不同商品
                    //将购物项加入之前的购物项中
                    oldOrderItemList.add(orderItem);
                }
            } else {
                //不属于同一个商家 将购物项加入之前的购物项中
                cartList.add(cart);
            }
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("登陆的用户名是" + username);
            if (!username.equals("anonymousUser")) {
                //已登录
                //将车子保存到redis中去(如果本地有购物车 ,进行同步)
                cartService.mergeCartList(username, cartList);
                //同步本地的购物车后,需要清空本地的购物车
                if (flag) {
                    Cookie cookie = new Cookie("BUYER_CART", null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");//cookie共享
                    response.addCookie(cookie);
                }
            } else {
                //未登录
                //将车子保存到cookie
                String sss = JSON.toJSONString(cartList);
                String encode = URLEncoder.encode(sss, "UTF-8");
                Cookie cookie = new Cookie("BUYER_CART", encode);
                cookie.setMaxAge(60 * 600);
                cookie.setPath("/");//cookie共享
                response.addCookie(cookie);
            }
            return new Result(true, "成功加入购物车");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "加入购物车失败");
        }

    }

    /**
     * 回显购物车的数据
     *
     * @param request
     * @return
     */
    @RequestMapping("/findCartList.do")
    public List<Cart> findCartList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        List<Cart> cartList = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            //从缓存中拿出来
            for (Cookie cookie : cookies) {
                //cookie 的数据  key-value(json串)
                if ("BUYER_CART".equals(cookie.getName())) {
                    //有 :从cookie中取出来
                    String value = cookie.getValue();
                    String decode = URLDecoder.decode(value, "UTF-8");
                    cartList = JSON.parseArray(decode, Cart.class);
                    break;
                }
            }
        }
        //判断用户是否登陆
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("回显时判断用户是否登陆" + name);
        if (!name.equals("anonymousUser")) {
            //场景：未登录将商品加入购物车
            // 如果用登录成功跳转到该页面--->【我的购物车】--->将本地的购物车同步到redis中
            if (cartList != null) {
                //已登录 本地cookie和redis同步
                cartService.mergeCartList(name, cartList);

                //清空cookie
                Cookie cookie = new Cookie("BUYER_CART", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);


            }
            //已登录 无缓存
            cartList = cartService.findCartListFromRedis(name);
        }


        if (cartList != null) {
            cartList = cartService.autoDataToCart(cartList);
        }
        return cartList;
    }
}
