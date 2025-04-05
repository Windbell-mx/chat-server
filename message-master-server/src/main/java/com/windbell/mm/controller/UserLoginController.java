package com.windbell.mm.controller;


import com.windbell.mm.model.Result;
import com.windbell.mm.model.entities.User;
import com.windbell.mm.service.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录实现
 */

@Tag(name = "个人管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserLoginController {

    private final UserLoginService userLoginService;

    @Operation(summary = "用户登录")
    @GetMapping("/login")
    public Result<String> login(@RequestParam String account, @RequestParam String password) {
        String token = userLoginService.login(account,password);
        return Result.ok(token);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        userLoginService.register(user);
        return Result.ok();
    }
}
