package com.alibaba.core.service.listener;

import com.alibaba.core.service.staticpage.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @ClassName PageListener
 * @Description 获取消息--消费消息
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 21:33 2019/3/30
 * @Version 2.1
 **/

public class PageListener implements MessageListener {

    @Resource
    private StaticPageService staticPageService;

    @Override
    public void onMessage(Message message) {
        try {
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
            String id = activeMQTextMessage.getText();
            System.out.println("page获取到消息的id" + id);


            //消费消息
            staticPageService.getHtml(Long.parseLong(id));


        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
