package com.windbell.mm.communication;

import com.windbell.mm.exception.MessageException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClientConfig {

    public void connect(String host, int port) {

        //客户端NIO线程组
        NioEventLoopGroup executors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(executors)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new BootNettyChannelInitializer<SocketChannel>());
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new MessageException(e.getMessage(), null);
        } finally {
            executors.shutdownGracefully();
        }
    }
}
