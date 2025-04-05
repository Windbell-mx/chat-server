package com.windbell.mm.controller;

import com.windbell.mm.config.DirectExchangeConfig;
import com.windbell.mm.config.FanoutExchangeConfig;
import com.windbell.mm.converter.MessageConverter;
import com.windbell.mm.model.Result;
import com.windbell.mm.model.VO.ChatMessageVo;
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

    /**
     * 发送消息
     * @param msg 消息内容
     * @return 返回值
     */
    @Operation(summary = "用户：8003")
    @PostMapping("/8003")
    public Result<String> sendMessage(@RequestParam String msg) {
        System.out.println("回复内容:\n");
        ChatMessageVo messageVo = ChatMessageHandler.create();
        messageVo.setMsg(msg);
        messageVo.setSender("用户8003");
        messageVo.setReceiver("用户8002");
        System.out.println(messageVo);
        System.out.println(messageVo.getSender() + ": " + messageVo.getMsg());
        messageConverter.rabbitTemplate(connectionFactory).convertAndSend(
                DirectExchangeConfig.DIRECT_EXCHANGE,
                DirectExchangeConfig.DIRECT_ROUTINGKEY, messageVo);
        return Result.ok();
    }

    /**
     * 监听来自服务端转发的消息
     * @param messageVo 消息对象
     */
    @RabbitListener(queues = FanoutExchangeConfig.FANOUT_QUEUE3)
    public void processMessage3(ChatMessageVo messageVo) {
            System.out.println(messageVo.getSender() + ": " + messageVo.getMsg());
    }
}
