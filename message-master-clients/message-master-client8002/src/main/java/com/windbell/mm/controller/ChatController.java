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
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQAutoConfig rabbitMQAutoConfig;
    @Resource(name = "rabbitListenerEndpointRegistry")
    private RabbitListenerEndpointRegistry registry;
    private final AmqpAdmin amqpAdmin;


    /**
     * 发送消息
     *
     * @param msg 消息内容
     * @return 返回值
     */
    @Operation(summary = "用户：8002")
    @PostMapping("/8002")
    public Result<String> sendMessage(@RequestParam String msg) {
        System.out.println("回复内容:");
        ChatMessageVo messageVo = ChatMessageHandler.create();
        messageVo.setMsg(msg);
        messageVo.setSender("用户8002");
        messageVo.setReceiver("用户8002");
        System.out.println(messageVo);
        messageConverter.rabbitTemplate(connectionFactory).convertAndSend(
                DirectExchangeConfig.DIRECT_EXCHANGE,
                DirectExchangeConfig.DIRECT_ROUTINGKEY, messageVo);

        System.out.println(rabbitMQAutoConfig.getUserQueues().get("8002"));

        return Result.ok();
    }

    /**
     * 监听来自服务端转发的消息
     *
     * @param messageVo 消息对象
     */
    @RabbitListener(queues = FanoutExchangeConfig.FANOUT_QUEUE2)
    public void processMessage2(ChatMessageVo messageVo) {
        System.out.println(messageVo.getSender() + ": " + messageVo.getMsg());
    }


    //@RabbitListener(queues = "#{rabbitMQAutoConfig.userQueues['8002']}",
    @RabbitListener(queues = "queue_user_8002")
    public void receivePrivateMessage(ChatMessageVo messageVo) {
        System.out.println("收到私聊消息：" + messageVo);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void setupQueues() {
        System.out.println("创建 RabbitMQ 队列...");
        rabbitMQAutoConfig.createUserQueue("8002", "");
        // 确保队列创建后才启动监听器
        registry.getListenerContainer("privateMessageListener").start();
    }
}
