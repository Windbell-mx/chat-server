package com.windbell.mm.model.entities;


import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;


/**
 * 用户自查朋友圈表
 */
@Data
@Entity
@Table(name = "t_friends_circle")
public class FriendsCircle implements Serializable {

    private static final long serializableVersionUID = 1L;

    /**
     * 发表人聊天号
     */
    private String account;
    /**
     * 说说唯一ID
     */
    @Id
    @TableId
    private String titleId;
    /**
     * 说说内容
     */
    private String context;
    /**
     * 发布时间
     */
    private String releaseTime;

}
