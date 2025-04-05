package com.windbell.mm.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * fanout交换机信息配置类
 */
@Configuration
public class FanoutExchangeConfig {

    //定义交换机名称，队列名称
    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    public static final String FANOUT_QUEUE1 = "fanout_queue1";
    public static final String FANOUT_QUEUE2 = "fanout_queue2";
    public static final String FANOUT_QUEUE3 = "fanout_queue3";

    //定义交换机
    @Bean("fanoutExchange")
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE).durable(true).build();
    }

    //定义消息队列1
    @Bean("fanoutQueue1")
    public Queue fanoutQueue1() {
        return QueueBuilder.durable(FANOUT_QUEUE1).build();
    }

    //定义消息队列2
    @Bean("fanoutQueue2")
    public Queue fanoutQueue2() {
        return QueueBuilder.durable(FANOUT_QUEUE2).build();
    }

    //定义消息队列2
    @Bean("fanoutQueue3")
    public Queue fanoutQueue3() {
        return QueueBuilder.durable(FANOUT_QUEUE3).build();
    }

    @Bean
    public Binding fanoutBinding1(@Qualifier("fanoutExchange") FanoutExchange exchange,
                                  @Qualifier("fanoutQueue1") Queue queue1) {
        return BindingBuilder.bind(queue1).to(exchange);
    }

    @Bean
    public Binding fanoutBinding2(@Qualifier("fanoutExchange") FanoutExchange exchange,
                                  @Qualifier("fanoutQueue2") Queue queue2) {
        return BindingBuilder.bind(queue2).to(exchange);
    }

    @Bean
    public Binding fanoutBinding3(@Qualifier("fanoutExchange") FanoutExchange exchange,
                                  @Qualifier("fanoutQueue3") Queue queue3) {
        return BindingBuilder.bind(queue3).to(exchange);
    }
}
