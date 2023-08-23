package com.zxwang.rocketmqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 创建配置类，可以初始化一个消息队列的生产者
 */
@Configuration
public class RabbitMQConfig {
    // 定义交换机的名称
    public static final String SMS_EXCHANGE="test_exchange";

    // 定义队列的名称
    public static final String SMS_QUEUE="test_queue";

    // 发送短信的routingkey
    public static final  String ROUTING_KEY_SMS_SEND = "key.user.test";

    // 创建交换机
    @Bean(SMS_EXCHANGE)
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(SMS_EXCHANGE).durable(true).build();
    }

    // 创建队列
    @Bean(SMS_QUEUE)
    public Queue queue(){
        return QueueBuilder.durable(SMS_QUEUE).build();
                // 关联死信队列
//                .withArgument("x-dead-letter-exchange",RabbitDeadMQSMSConfig.SMS_EXCHANGE_DEAD)
//                .withArgument("x-dead-letter-routing-key",RabbitDeadMQSMSConfig.ROUTING_KEY_SMS_SEND_DEAD)
                // 队列的最大长度，超出的部分之后变为死信
//                .withArgument("x-max-length",6).build();
        // 创建队列时统一增加配置，设定消息超时时间
//        return QueueBuilder.durable(SMS_QUEUE).withArgument("x-message-ttl",10*1000).build();
    }




    // 创建绑定关系
    @Bean()
    public Binding binding(@Qualifier(SMS_EXCHANGE) Exchange exchange, @Qualifier(SMS_QUEUE) Queue queue){
        return  BindingBuilder.bind(queue).to(exchange).with("key.user.#").noargs();
    }
}
