package com.zxwang.rocketmqdemo.mq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.zxwang.rocketmqdemo.config.RabbitMQConfig;
import com.zxwang.rocketmqdemo.pojo.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 定义消费者
 */
@Component
public class RabbitMQConsumer implements ChannelAwareMessageListener {
    // 监听指定的队列
    @RabbitListener(queues = RabbitMQConfig.SMS_QUEUE)
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            // 获取消息内容
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            System.out.println("routingKey:"+routingKey);
            System.out.println(message.getBody());
            User user = JSONObject.parseObject(message.getBody(),User.class);
            System.out.println("收到消息：" + user);
            // 处理业务逻辑
            // ...
            // 手动确认消息已消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            // 手动拒绝消息，可以设置是否重新入队列
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,false);
        }
    }


}
