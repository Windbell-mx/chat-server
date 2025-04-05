package com.windbell.mm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.windbell.mm.enums.ResultCodeEnum;
import com.windbell.mm.exception.MessageException;
import com.windbell.mm.mapper.UserRelationshipMapper;
import com.windbell.mm.model.entities.UserRelationship;
import com.windbell.mm.redis.RedisService;
import com.windbell.mm.service.UserRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRelationshipServiceImpl implements UserRelationshipService {

    private final UserRelationshipMapper userRelationshipMapper;
    private final RedisService redisService;

    /**
     * 获取好友列表
     * @param account 本人账号
     * @return 返回好友列表集合
     */
    @Override
    public List<UserRelationship> getFriendList(String account) {
        UserRelationship[] array = redisService.get("getFriendList",UserRelationship[].class);
        List<UserRelationship> list = array != null ? Arrays.asList(array) : new ArrayList<>();
        if (list.isEmpty()) {
            LambdaQueryWrapper<UserRelationship> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserRelationship::getAccount, account);
            list = userRelationshipMapper.selectList(queryWrapper);
            redisService.set("getFriendList",list,3600);
        }
        return list;
    }

    /**
     * 根据好友账号删除好友
     * @param friendAccount 好友
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delFriend(String friendAccount) {
        int i = userRelationshipMapper.delete(
                new LambdaQueryWrapper<UserRelationship>()
                        .eq(UserRelationship::getAccount, friendAccount));
        if (i != 1) {
            throw new MessageException(ResultCodeEnum.SERVICE_ERROR);
        }
    }
}
