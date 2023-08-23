package com.zxwang.rocketmqdemo.mq;

import com.zxwang.rocketmqdemo.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMQTest {

    @Autowired
    private RabbitMQProducer producer;

    @Test
    void testSend() {
        User user = new User("Alice", 20);
        producer.send(user);
    }
}
