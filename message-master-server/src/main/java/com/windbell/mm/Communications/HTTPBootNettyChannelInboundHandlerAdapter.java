package com.windbell.mm.Communications;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * http请求连接处理
 */

@Slf4j
public class HTTPBootNettyChannelInboundHandlerAdapter extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        final String uri = fullHttpRequest.uri();
        System.out.println(uri);
        switch (fullHttpRequest.method().name()) {
            case "GET":
                processGetRequest(fullHttpRequest);
                break;
            case "POST":
                if (fullHttpRequest.headers().get("Content-Type").contains("x-www-form-urlencoded")) {
                    processPostFormRequest(fullHttpRequest);
                } else if (fullHttpRequest.headers().get("Content-Type").contains("application/json")) {
                    processPostJsonRequest(fullHttpRequest);
                }
                break;
            default:
        }
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        buf.writeCharSequence("sucess", StandardCharsets.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set("Content-Type", "application/json;charset=UTF-8");
        response.headers().set("Content-Length", response.content().readableBytes());

        channelHandlerContext.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 处理get请求
     *
     * @param request 请求
     */
    private void processGetRequest(FullHttpRequest request) {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> params = decoder.parameters();
        params.forEach((key, value) -> System.out.println(key + " ==> " + value));
    }

    /**
     * 处理post form请求
     *
     * @param request 请求
     */
    private void processPostFormRequest(FullHttpRequest request) {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
        List<InterfaceHttpData> httpDataList = decoder.getBodyHttpDatas();
        httpDataList.forEach(item -> {
            if (item.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                Attribute attribute = (Attribute) item;
                try {
                    System.out.println(attribute.getName() + " ==> " + attribute.getValue());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

            }
        });
    }

    /**
     * 处理post json请求
     *
     * @param request 请求
     */
    private void processPostJsonRequest(FullHttpRequest request) {
        ByteBuf content = request.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);

        JSONObject jsonObject = JSONObject.parseObject(new String(bytes));
        jsonObject.getInnerMap().forEach((key, value) -> System.out.println(key + " ==> " + value));
    }
}



