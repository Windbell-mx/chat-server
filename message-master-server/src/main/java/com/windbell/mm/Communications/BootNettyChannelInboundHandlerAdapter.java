package com.windbell.mm.Communications;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootNettyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

    //private final RabbitTemplate rabbitTemplate;
    /**
     * 从客户端收到新的数据时，这个方法会在收到消息时被调用
     * @param ctx 信道处理器回应
     * @param msg 回应内容
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead:read msg: "+msg);
        System.out.println("回复内容:");
        //将消息转发到server端 -> client端的交换机
        //rabbitTemplate.convertAndSend(FanoutExchangeConfig.FANOUT_EXCHANGE, "",msg);
        ctx.flush();
    }

    /**
     * 从客户端收到数据，读取完成时调用
     * @param ctx 信道处理器回应
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete: 消息读取完成");
        ctx.flush();
    }

    /**
     * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
     * @param ctx 信道处理器回应
     * @param cause 原因
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        //抛出异常，断开与客户端的连接
        ctx.close();
    }

    /**
     * 客户端与服务端第一次建立连接时 执行
     * @param ctx 信道处理器回应
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.channel().read();
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        //此处不能使用ctx.close()，否则客户端始终无法与服务端建立连接
        System.out.println("channelActive: "
                + inetSocketAddress.getAddress()
                + ": " + inetSocketAddress.getPort());
        byte[] req = ("你好，欢迎上线!").getBytes();
        ByteBuf message = Unpooled.buffer(req.length);
        message.writeBytes(req);
        ctx.writeAndFlush(message);
    }

    /**
     * 客户端与服务端 断连时 执行
     * @param ctx 信道处理器回应
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = inetSocketAddress.getAddress().getHostAddress();
        //断开连接时，必须关闭，否则造成资源浪费，并发量很大情况下可能造成宕机
        ctx.close();
        System.out.println("channelInactive:" + clientIP + ctx.name());
    }

    /**
     * 服务端当read超时, 会调用这个方法
     * @param ctx 信道处理器回应
     * @param evt 消息事件
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = inetSocketAddress.getAddress().getHostAddress();
        //超时时断开连接
        ctx.close();
        System.out.println("userEventTriggered:" + clientIP + ctx.name());
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

    /**
     * 当Channel从EventLoop注销后触发
     * 用途：资源清理、状态重置
     * @param ctx Netty的通道处理上下文对象，包含通道、事件循环等关联信息
     * @throws Exception 可能触发的异常
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
    }

    /**
     * 通道可写状态变化时（写缓冲区水位变化）
     * 用途：流量控制、防止OOM
     * @param ctx Netty的通道处理上下文对象，包含通道、事件循环等关联信息
     * @throws Exception 可能触发的异常
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelWritabilityChanged");
    }

}
