package com.windbell.mm.communication;

import com.windbell.mm.converter.MessageConverter;
import com.windbell.mm.model.VO.ChatMessageVo;
import com.windbell.mm.rabbitMQAutoConfig.RabbitMQAutoConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootNettyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final RabbitMQAutoConfig rabbitMQAutoConfig;
    /**
     * 从服务端收到新的数据时，这个方法会在收到消息时被调用
     *
     * @param ctx 信道处理器回应
     * @param msg 回应内容
     * @throws Exception 异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead:read msg:" + msg.toString());
        //回应服务端
    }

    /**
     * 从服务端收到新的数据、读取完成时调用
     *
     * @param ctx 信道处理器回应
     * @throws Exception 异常
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        ctx.flush();
    }

    /**
     * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
     *
     * @param ctx   信道处理器回应
     * @param cause 异常原因
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        log.error(cause.getMessage(), cause);
        //抛出异常，断开与客户端的连接
        ctx.close();
    }

    /**
     * 客户端与服务端第一次建立连接时 执行
     *
     * @param ctx 信道处理器回应
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = inetSocketAddress.getAddress().getHostAddress();
        System.out.println("channelActive: " + clientIP + ": " + inetSocketAddress.getPort());
        rabbitMQAutoConfig.createUserQueue("8002","");
    }

    /**
     * 客户端与服务端 断连时 执行
     *
     * @param ctx 信道处理器回应
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inetSocketAddress.getAddress().getHostAddress();
        //断开连接时，必须关闭，否则造成资源浪费
        ctx.close();
        System.out.println("channelInactive:" + clientIp);
    }

    /**
     * 当channel成功注册到EventLoop时触发，此时通道还没有绑定到具体的地址
     * 用途：初始化资源、建立关联数据结构
     * @param ctx Netty的通道处理上下文对象，包含通道、事件循环等关联信息
     * @throws Exception 可能触发的异常
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
    }
}
