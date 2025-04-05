package com.windbell.mm.utils;

import java.util.UUID;

public class MyUUID {

    //使用随机生成uuid
    public static String create(int start,int end) {
        UUID uuid = UUID.randomUUID();
        // 转换为字符串并截取前 end-start 个字符
        return uuid.toString().replace("-", "").substring(start, end);
    }
}
