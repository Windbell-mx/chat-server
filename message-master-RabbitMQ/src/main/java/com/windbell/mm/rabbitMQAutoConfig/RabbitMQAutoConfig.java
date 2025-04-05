package com.windbell.mm.rabbitMQAutoConfig;


import com.windbell.mm.model.VO.ChatMessageVo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录时登动态创建交换机和消息队列
 */
@Service
@Data
public class RabbitMQAutoConfig {

    private final RabbitAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;

    // 交换机名称
    private static final String DIRECT_EXCHANGE_PREFIX = "direct_exchange_user_";
    private static final String FANOUT_EXCHANGE_PREFIX = "fanout_exchange_group_";

    // 存储用户的专属队列名
    public final Map<String, String> userQueues = new HashMap<>();

    public RabbitMQAutoConfig(RabbitAdmin rabbitAdmin, RabbitTemplate rabbitTemplate) {
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;

    }

    /**
     * 用户登录时，动态创建直连队列和群聊队列
     */
    public void createUserQueue(String userId, String groupId) {
        // 创建用户专属队列
        String userQueueName = "queue_user_" + userId;
        Queue userQueue = new Queue(userQueueName, true);
        rabbitAdmin.declareQueue(userQueue);

        // 创建直连交换机
        DirectExchange directExchange = new DirectExchange(DIRECT_EXCHANGE_PREFIX + userId);
        rabbitAdmin.declareExchange(directExchange);
        rabbitAdmin.declareBinding(BindingBuilder.bind(userQueue).to(directExchange).with(userId));

        // 记录用户的队列信息
        userQueues.put(userId, userQueueName);

        // 处理群聊（如果群 ID 存在）
        if (groupId != null) {
            createGroupQueue(userQueueName, groupId);
        }
    }

    /**
     * 用户加入群聊时，将其绑定到 Fanout 交换机
     */
    public void createGroupQueue(String userQueueName, String groupId) {
        // 确保群聊交换机存在
        FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_EXCHANGE_PREFIX + groupId);
        rabbitAdmin.declareExchange(fanoutExchange);

        // 绑定用户队列到群聊交换机
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(userQueueName)).to(fanoutExchange));
    }

    /**
     * 用户退出时，删除队列和交换机
     */
    public void deleteUserQueue(String userId) {
        String userQueueName = userQueues.get(userId);
        if (userQueueName != null) {
            rabbitAdmin.deleteQueue(userQueueName);
            rabbitAdmin.deleteExchange(DIRECT_EXCHANGE_PREFIX + userId);
            userQueues.remove(userId);
        }
    }

    /**
     * 发送私聊消息
     */
    public void sendPrivateMessage(String senderId, String receiverId, ChatMessageVo messageVo) {
        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_PREFIX + receiverId, receiverId, messageVo);
    }

    /**
     * 发送群聊消息
     */
    public void sendGroupMessage(String groupId, ChatMessageVo messageVo) {
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_PREFIX + groupId, "", messageVo);
    }

}
