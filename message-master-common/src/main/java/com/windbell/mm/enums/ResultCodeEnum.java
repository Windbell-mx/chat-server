package com.windbell.mm.enums;


import lombok.Getter;


/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    PARAM_ERROR(202, "参数不正确"),
    SERVICE_ERROR(203, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),

    ACCOUNT_EXIST_ERROR(301, "账号已存在"),
    CAPTCHA_CODE_ERROR(302, "验证码错误"),
    CAPTCHA_CODE_EXPIRED(303, "验证码已过期"),
    CAPTCHA_CODE_NOT_FOUND(304, "未输入验证码"),

    LOGIN_AUTH(305, "未登陆"),
    ACCOUNT_NOT_EXIST_ERROR(306, "账号不存在"),
    ACCOUNT_ERROR(307, "用户名或密码错误"),
    ACCOUNT_DISABLED_ERROR(308, "该用户已被禁用"),
    ACCESS_FORBIDDEN(309, "无访问权限"),

    ACCOUNT_AND_PASSWORD_NOT_EMPTY(501,"账号和密码不能为空"),
    LOGIN_PHONE_EMPTY(502, "手机号码为空"),
    LOGIN_CODE_EMPTY(503, "验证码为空"),
    SEND_SMS_TOO_OFTEN(504, "验证码发送过于频繁"),

    TOKEN_EXPIRED(601, "token已过期，请重新登录"),
    TOKEN_INVALID(602, "token签名无效");


    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
