package com.windbell.mm.chatMessage;

import com.windbell.mm.config.DirectExchangeConfig;
import com.windbell.mm.config.FanoutExchangeConfig;
import com.windbell.mm.converter.MessageConverter;
import com.windbell.mm.model.VO.ChatMessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ChatMessageReceiver {
    private final MessageConverter messageConverter;
    private final ConnectionFactory connectionFactory;

    /**
     * 处理消息
     * @param messageVo 被rabbitmq封装后的消息
     */
    @RabbitListener(queues = DirectExchangeConfig.DIRECT_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    public void processMessage(ChatMessageVo messageVo) {
        //将消息转发到server端 -> client端的交换机
        messageConverter.rabbitTemplate(connectionFactory).convertAndSend(
                FanoutExchangeConfig.FANOUT_EXCHANGE, "",messageVo);
    }
}
