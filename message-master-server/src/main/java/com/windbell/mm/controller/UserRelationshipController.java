package com.windbell.mm.controller;


import com.windbell.mm.model.Result;
import com.windbell.mm.model.entities.UserRelationship;
import com.windbell.mm.service.UserRelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 好友列表实现
 */

@Tag(name = "好友管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/userRelationship")
public class UserRelationshipController {

    private final UserRelationshipService userRelationshipService;

    @Operation(summary = "查询好友列表")
    @GetMapping("/friendList")
    public Result<List<UserRelationship>> friendList(@RequestParam String account) {
        List<UserRelationship> list = userRelationshipService.getFriendList(account);
        return Result.ok(list);
    }

    @Operation(summary = "根据用户号删除好友")
    @DeleteMapping("/delFriend")
    public Result<String> delFriend(@RequestParam String friendAccount) {
        userRelationshipService.delFriend(friendAccount);
        return Result.ok();
    }

    //待实现
    @Operation(summary = "根据用户好添加好友")
    @PostMapping("/addFriend")
    public Result<String> addFriend(@RequestParam String addAccount) {
        return Result.ok();
    }
}
