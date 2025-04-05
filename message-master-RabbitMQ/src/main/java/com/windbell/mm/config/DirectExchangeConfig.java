package com.windbell.mm.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 直连交换机信息配置类
 */
@Configuration
public class DirectExchangeConfig {

    //定义交换机名称，队列名称，路由键名称
    public static final String DIRECT_EXCHANGE = "direct.exchange";
    public static final String DIRECT_QUEUE = "direct.queue";
    public static final String DIRECT_ROUTINGKEY = "direct.routingKey";

    //定义交换机
    @Bean("directExchange")
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange(DIRECT_EXCHANGE).durable(true).build();
    }

    //定义队列
    @Bean("directQueue")
    public Queue directQueue() {
        return QueueBuilder.durable(DIRECT_QUEUE).build();
    }

    //绑定交换机和队列
    //@Qualifier 使用此注解可以以交换机和队列名绑定关系，便于区分
    @Bean
    public Binding directBinding(@Qualifier("directExchange") Exchange exchange,
                                 @Qualifier("directQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(DIRECT_ROUTINGKEY).noargs();
    }
}
