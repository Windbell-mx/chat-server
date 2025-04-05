package com.windbell.mm.controller;


import com.windbell.mm.config.DirectExchangeConfig;
import com.windbell.mm.config.FanoutExchangeConfig;
import com.windbell.mm.converter.MessageConverter;
import com.windbell.mm.model.Result;
import com.windbell.mm.model.VO.ChatMessageVo;
import com.windbell.mm.rabbitMQAutoConfig.RabbitMQAutoConfig;
import com.windbell.mm.utils.chatMessageUtil.ChatMessageHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "聊天管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final MessageConverter messageConverter;
    private final ConnectionFactory connectionFactory;
    private final RabbitMQAutoConfig rabbitMQAutoConfig;

    /**
     * 发送消息
     * @param msg 消息内容
     * @return 返回值
     */
    @Operation(summary = "用户：8001")
    @PostMapping("/8001")
    public Result<String> sendMessage(@RequestParam String msg) {
        System.out.println("回复内容:");
        ChatMessageVo messageVo = ChatMessageHandler.create();
        messageVo.setMsg(msg);
        messageVo.setSender("用户8001");
        messageVo.setReceiver("用户8002");
        System.out.println(messageVo);
/*        messageConverter.rabbitTemplate(connectionFactory).convertAndSend(
                DirectExchangeConfig.DIRECT_EXCHANGE,
                DirectExchangeConfig.DIRECT_ROUTINGKEY, messageVo);*/

        rabbitMQAutoConfig.sendPrivateMessage("8001","8002",messageVo);
        return Result.ok();
    }

    /**
     * 监听接收消息的队列
     * @param messageVo 被封装的消息体
     */
    @RabbitListener(queues = FanoutExchangeConfig.FANOUT_QUEUE1,
            containerFactory = "rabbitListenerContainerFactory")
    public void processMessage1(ChatMessageVo messageVo) {
            System.out.println(messageVo.getSender() + ": " + messageVo.getMsg());
    }
}
