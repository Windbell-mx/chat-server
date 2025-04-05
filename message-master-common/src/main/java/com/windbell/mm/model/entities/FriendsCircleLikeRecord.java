package com.windbell.mm.model.entities;


import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * 朋友圈点赞记录表
 */
@Data
@Table(name = "t_friends_circle_like_record")
public class FriendsCircleLikeRecord implements Serializable {

    private static final long serializableVersionUID = 1L;

    /**
     *聊天号
     */
    private String account;
    /**
     *发表人聊天号
     */
    private String publishedByAccount;
    /**
     *说说唯一ID
     */
    private String titleId;
    /**
     *说说内容
     */
    private String context;
    /**
     *我的评论
     */
    private String myComment;
    /**
     *点赞状态，0表示未点赞，1表示已点赞，默认0
     */
    private int my_like;
}
