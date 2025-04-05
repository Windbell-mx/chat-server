package com.windbell.mm.Communications;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *  通道初始化类
 */
@Component
@RequiredArgsConstructor
public class BootNettyChannelInitializer extends ChannelInitializer<Channel> {

    //private final RabbitTemplate rabbitTemplate;

    @Override
    protected void initChannel(Channel channel) throws Exception {

        //添加编解码，此处代码为解析tcp传过来的参数，为utf-8格式，可自定义编解码格式
        // ChannelOutboundHandler，依照逆序执行
        channel.pipeline().addLast("encoder", new StringEncoder());
        // 属于ChannelInboundHandler，依照顺序执行
        channel.pipeline().addLast("decoder", new StringDecoder());

        //自定义ChannelInboundHandlerAdapter
        channel.pipeline().addLast(new BootNettyChannelInboundHandlerAdapter());

        System.out.println("Channel initialized: " + channel);
    }
}
