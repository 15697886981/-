package com.alibaba.core.service.listener;

import com.alibaba.core.service.search.ItemSearchService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;


/**
 * @ClassName ItemSearchListener
 * @Description 获取消息--消费消息
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 21:04 2019/3/30
 * @Version 2.1
 **/
public class ItemSearchListener implements MessageListener {
    @Resource
    private ItemSearchService itemSearchService;


    @Override
    public void onMessage(Message message) {
        try {
            //取出消息体
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
            String id = activeMQTextMessage.getText();
            System.out.println("消费者获取到的id" + id);


            //消费消息
            itemSearchService.addItemToSolr(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
