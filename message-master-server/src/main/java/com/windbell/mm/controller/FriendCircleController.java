package com.windbell.mm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windbell.mm.mapper.FriendCircleMapper;
import com.windbell.mm.minio.MinioService;
import com.windbell.mm.model.Result;
import com.windbell.mm.model.entities.FriendsCircle;
import com.windbell.mm.model.entities.FriendsCircleLikeRecord;
import com.windbell.mm.service.FriendCircleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 朋友圈实现
 */

@Tag(name = "动态管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/friendCircle")
public class FriendCircleController {

    private final FriendCircleService friendCircleService;
    private final MinioService minioService;

    @Operation(summary = "查询所有朋友动态")
    @GetMapping("/dynamicList")
    public Result<IPage<FriendsCircle>> dynamicList(@RequestParam long current, @RequestParam long size,FriendsCircle friendCircle) {
        Page<FriendsCircle> page = new Page<>(current, size);
        IPage<FriendsCircle> list = friendCircleService.getDynamicList(page,friendCircle);
        return Result.ok(list);
    }

    @Operation(summary = "根据账号查询动态 ")
    @GetMapping("/getDynamicByAccount")
    public Result<List<FriendsCircle>> getDynamicByAccount(HttpServletRequest request) {
        String account = (String) request.getAttribute("currentUser");
        System.out.println(account);
        List<FriendsCircle> list = friendCircleService.getDynamicByAccount(account);
        return Result.ok(list);
    }

    @Operation(summary = "发表动态")
    @PostMapping("/publish")
    public Result<String> publish(@RequestBody FriendsCircle friendCircle, MultipartFile file) {
        minioService.uploadFile(file);
        friendCircleService.publish(friendCircle);
        return Result.ok();
    }

    @Operation(summary = "点赞评论")
    @PostMapping("/likeAndComment")
    public Result<String> likeAndComment(@RequestParam String account, @RequestParam String titleId, int like,String comment) {
        //String account = (String) request.getAttribute("currentUser");
        friendCircleService.likeAndComment(account,titleId,like,comment);
        return Result.ok();
    }

    @Operation(summary = "取消点赞")
    @PostMapping("/cancel")
    public Result<String> cancel(@RequestBody FriendsCircleLikeRecord record) {
        friendCircleService.cancel(record);
        return Result.ok();
    }
}
