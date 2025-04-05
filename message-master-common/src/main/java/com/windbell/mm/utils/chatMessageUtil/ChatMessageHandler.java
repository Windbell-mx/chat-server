package com.windbell.mm.utils.chatMessageUtil;

import com.windbell.mm.model.VO.ChatMessageVo;
import com.windbell.mm.utils.SnowflakeIdGenerator;

/**
 * 消息封装类，用于封装消息内容，便于传输
 */

public class ChatMessageHandler {

    /**
     * 生成一个消息处理器实例
     * @return 返回消息处理器
     */
    public static ChatMessageVo create() {
        //使用雪花算法生成ID
        long id = new SnowflakeIdGenerator(15672, 7764).nextId();
        ChatMessageVo chatMessageVo = new ChatMessageVo();
        //设置消息唯一ID
        chatMessageVo.setMsg_id(id);
        //生成当前时间戳
        chatMessageVo.setTimeStamp(System.currentTimeMillis());
        //返回消息处理器实例
        return chatMessageVo;

    }
}
