package com.windbell.mm.model.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public ChatMessageVo(){

    }
    /**
     * 唯一消息ID，使用雪花算法生成
     */
    private long msg_id;
    /**
     * 消息发送者ID
     */
    private String sender;
    /**
     * 消息接收者/群组ID
     */
    private String receiver;
    /**
     * 消息体
     */
    private String msg;
    /**
     * 版本时间戳，处理消息顺序问题
     */
    private long timeStamp;
}
