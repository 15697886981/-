package com.alibaba.core.service.listener;

import com.alibaba.core.service.search.ItemSearchService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @ClassName ItemDeleteListener
 * @Description 获取消息--消费消息
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 15:36 2019/3/31
 * @Version 2.1
 **/


public class ItemDeleteListener implements MessageListener {

    @Resource
    private ItemSearchService itemSearchService;


    @Override
    public void onMessage(Message message) {
        try {
            //获取消息
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
            String id = activeMQTextMessage.getText();

            //消费消息
            itemSearchService.deleteItemFromSolr(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
