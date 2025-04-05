package com.windbell.mm.Communications;


import com.windbell.mm.converter.MessageConverter;
import com.windbell.mm.exception.MessageException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * nettyServer端启动类
 */
@Component
@RequiredArgsConstructor
public class NettyServerConfig {

    //private final RabbitTemplate rabbitTemplate;
    //private final MessageConverter messageConverter;

    /**
     * 配置服务端的NIO线程组
     * NioEventLoopGroup 是用来处理I/O操作的Reactor线程组
     * bossGroup：用来接收进来的连接，workerGroup：用来处理已经被接收的连接,进行socketChannel的网络读写，
     * bossGroup接收到连接后就会把连接信息注册到workerGroup
     * workerGroup的EventLoopGroup默认的线程数是CPU核数的二倍
     *
     * @param port 需要传入需要绑定的端口号
     */
    public void NettyServerBind(int port) throws MessageException {

        //监听客户端连接，专门负责与客户端创建连接，并把连接注册到workerGroup的selector中
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //处理每一个连接发生的读写事件
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务端启动对象 设置两个线程组
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    //绑定服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //ChannelInitializer初始化通道SocketChannel
                    .childHandler(new BootNettyChannelInitializer());

            //绑定端口，启动服务器
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("NettyServer running at port 8001 (●'◡'●)");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new MessageException(e.getMessage(), null);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
