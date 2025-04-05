package com.windbell.mm.exception;

import com.windbell.mm.enums.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class MessageException extends RuntimeException {



    //异常状态码
    private final Integer code;
    /**
     * 通过状态码和错误消息创建异常对象
     * @param message 提示信息
     * @param code 提示码
     */
    public MessageException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 根据响应结果枚举对象创建异常对象
     * @param resultCodeEnum 枚举类实例
     */
    public MessageException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    /**
     * 自定义异常信息
     * @param message 异常信息
     */
    public MessageException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "LeaseException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
