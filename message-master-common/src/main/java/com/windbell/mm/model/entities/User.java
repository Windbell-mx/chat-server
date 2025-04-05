package com.windbell.mm.model.entities;


import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;


/**
 * 用户表
 */
@Data
@Entity
@Table(name = "t_user")
public class User implements Serializable {

    private static final long serializableVersionUID = 1L;

    /**
     * 聊天号
     */
    @Id
    @TableId
    private String account;
    /**
     * 码账号密
     */
    private String password;
    /**
     * 昵称
     */
    private String NickName;
    /**
     * 性别
     */
    private String gender;
    /**
     * 个性签名
     */
    private String sign;
    /**
     * 地区
     */
    private Integer area;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 二维码
     */
    @Column(name = "QR")
    private String qr;
}

