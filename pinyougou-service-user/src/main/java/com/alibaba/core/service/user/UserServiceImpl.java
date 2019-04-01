package com.alibaba.core.service.user;

import com.alibaba.core.dao.user.UserDao;
import com.alibaba.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.utils.core.md5.MD5Util;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserService
 * @Description 用户服务实现
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 19:23 2019/3/31
 * @Version 2.1
 **/
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private ActiveMQQueue smsDestination;

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Resource
    private UserDao userDao;

    /**
     * 发送验证码
     *
     * @param phone
     */
    @Override
    public void sendCode(final String phone) {

        final String code = RandomStringUtils.randomNumeric(6);
        System.out.println("你的验证码是:" + code);

        //把session存入redis中
        redisTemplate.boundValueOps(phone).set(code);
        //设置过期时间
        redisTemplate.boundValueOps(phone).expire(5, TimeUnit.MINUTES);


        // 保存验证码到redis中
        // 使用的数据结构：String  set key（phone） value（code）
        //redisTemplate.boundValueOps(phone).set(code);
        // 设置验证码的失效时间(redis数据过期：设置key过期，expire)
        //redisTemplate.boundValueOps(phone).expire(5, TimeUnit.MINUTES);


        //将数据发送到mq中
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("phoneNumbers", phone);
                mapMessage.setString("signName", "品优购");
                mapMessage.setString("templateCode", "SMS_162522315");
                mapMessage.setString("templateParam", "{\"code\":\"" + code + "\"}");
                return mapMessage;
            }
        });
    }

    /**
     * 用户注册
     *
     * @param smscode
     * @param user
     */
    @Override
    public void add(String smscode, User user) {
        //判断验证码是否正确

        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();

        if (code != null && !smscode.equals("") && smscode.equals(code)) {

            //验证码正确
            //保存用户
            String password = MD5Util.MD5Encode(user.getPassword(), null);
            user.setPassword(password);
            user.setCreated(new Date());
            user.setUpdated(new Date());
            userDao.insertSelective(user);
        } else {
            throw new RuntimeException("验证码不正确");
        }
    }
}
