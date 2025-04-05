package com.windbell.mm;


import com.windbell.mm.config.DirectExchangeConfig;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith({SpringExtension.class})
public class RabbitMQTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @org.junit.jupiter.api.Test
    public void test() {
        rabbitTemplate.convertAndSend(
                DirectExchangeConfig.DIRECT_EXCHANGE,
                DirectExchangeConfig.DIRECT_ROUTINGKEY,
                "hello");
    }

    @Test
    @RabbitListener(queues = DirectExchangeConfig.DIRECT_QUEUE)
    public void getMessage(Message message) {
        System.out.println(new String(message.getBody()));
    }
}
