package com.windbell.mm.interceptor;


import com.windbell.mm.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;


public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String token = request.getHeader("access-token");

        String account = JWTUtil.parseJwtToken(token);
        System.out.println(account);
        request.setAttribute("currentUser", account);
        return true;

    }
}
