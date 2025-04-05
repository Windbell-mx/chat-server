package com.windbell.mm.controller;


import com.windbell.mm.minio.MinioService;
import com.windbell.mm.model.Result;
import com.windbell.mm.model.entities.User;
import com.windbell.mm.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "个人管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/info")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final MinioService minioService;

    @Operation(summary = "修改个人信息")
    @PostMapping("/update")
    public Result<String> saveOrUpdate(@RequestBody User user) {
        userInfoService.saveOrUpdate(user);
        return Result.ok();
    }

    //还没实现
    @Operation(summary = "修改头像")
    @PostMapping("/update/avatar")
    public Result<String> saveOrUpdateAvatar(HttpServletRequest request,MultipartFile file) {
        //设置头像图片名为用户账号
        String account = (String) request.getAttribute("currentUser");
        minioService.uploadFile(file,account);
        return Result.ok();
    }
}
