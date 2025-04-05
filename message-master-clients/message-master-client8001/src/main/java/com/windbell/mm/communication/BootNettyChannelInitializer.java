package com.windbell.mm.communication;

import com.windbell.mm.rabbitMQAutoConfig.RabbitMQAutoConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootNettyChannelInitializer extends ChannelInitializer<Channel> {

    private final RabbitMQAutoConfig rabbitMQAutoConfig;
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void initChannel(Channel channel) throws Exception {
        //添加编解码，此处代码为解析tcp传过来的参数，为utf-8格式，可自定义编解码格式
        // ChannelOutboundHandler，依照逆序执行
        channel.pipeline().addLast("encoder", new StringEncoder());
        // 属于ChannelInboundHandler，依照顺序执行
        channel.pipeline().addLast("decoder", new StringDecoder());

        //自定义ChannelInboundHandlerAdapter
        channel.pipeline().addLast(new BootNettyChannelInboundHandlerAdapter(rabbitMQAutoConfig));
        channels.add(channel);
    }

    /**
     * 将channel保存到容器
     * @return 返回一个存有channel的容器
     */
    public static ChannelGroup getChannelGroup() {
        return channels;
    }
}
