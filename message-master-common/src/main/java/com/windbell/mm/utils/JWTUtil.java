package com.windbell.mm.utils;

import com.windbell.mm.enums.ResultCodeEnum;
import com.windbell.mm.exception.MessageException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;


import java.security.KeyPair;
import java.util.Date;

@Slf4j
public class JWTUtil {

    // 生成密钥对
    static KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
    /**
     * 生成token
     * @return token
     */

    public static String createJwtToken(String account) {

        return Jwts.builder()
                .subject(account)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(keyPair.getPrivate(), Jwts.SIG.RS256) // 指定私钥和算法
                .compact();
    }

    /**
     *
     * @param token 用户校验token
     * @return 返回解析结果
     */

    public static String parseJwtToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(keyPair.getPublic()) // 使用公钥验证
                    .build()
                    .parseSignedClaims(token);

            Claims claims = claimsJws.getPayload();
            System.out.println("用户标识: " + claims.getSubject());
            return claims.getSubject();
        } catch (Exception e) {
            if (e.getClass() == ExpiredJwtException.class) {
                throw new MessageException(ResultCodeEnum.TOKEN_EXPIRED);
            } else {
                throw new MessageException(ResultCodeEnum.TOKEN_INVALID);
            }
        }
    }
}
