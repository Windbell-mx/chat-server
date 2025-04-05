package com.windbell.mm.model.entities;


import lombok.Data;

import java.io.Serializable;


/**
 * 用户好友关系表
 */
@Data
public class UserRelationship implements Serializable {

    private static final long serializableVersionUID = 1L;

    /**
     * 本人聊天号
     */
    private String account;
    /**
     *好友账号
     */
    private String friendAccount;
    /**
     *好友昵称
     */
    private String friendNickName;
    /**
     *好友性别
     */
    private String friendSign;
    /**
     * 好友个性签名
     */
    private String friendGender;
    /**
     *好友地区
     */
    private Integer friendArea;
    /**
     *好友手机号
     */
    private String friendPhoneNumber;

}
