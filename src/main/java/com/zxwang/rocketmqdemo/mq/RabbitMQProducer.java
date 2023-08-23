package com.zxwang.rocketmqdemo.mq;

import com.alibaba.fastjson.JSONObject;
import com.zxwang.rocketmqdemo.config.RabbitMQConfig;
import com.zxwang.rocketmqdemo.pojo.User;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 生产者
 */
@Component
public class RabbitMQProducer  implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(User user) {
        // 设置回调函数
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this::returnedMessage);
        // 创建关联数据对象，用于标识消息
        String uuid =UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);
        // 发送消息到交换机，指定路由键
        rabbitTemplate.convertAndSend(RabbitMQConfig.SMS_EXCHANGE, RabbitMQConfig.ROUTING_KEY_SMS_SEND, JSONObject.toJSONString(user), correlationData);
    }

    /**
     *
     * @param correlationData 相关数据
     * @param ack  交换机是否成功接收数据
     * @param cause  失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("消息发送成功，correlationData=" + correlationData);
        } else {
            System.out.println("消息发送失败，correlationData=" + correlationData + ", cause=" + cause);
        }
    }

    /**
     * 定义return 回调
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        System.out.println("进入returnedMessage");
        System.out.println("消息未路由，message=" + returnedMessage.getMessage() + ", replyCode=" + returnedMessage.getReplyCode()
                + ", replyText=" + returnedMessage.getReplyText() + ", exchange=" + returnedMessage.getExchange() + ", routingKey=" + returnedMessage.getRoutingKey());

    }
}
