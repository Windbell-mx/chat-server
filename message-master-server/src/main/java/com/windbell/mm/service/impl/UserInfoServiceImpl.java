package com.windbell.mm.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.windbell.mm.enums.ResultCodeEnum;
import com.windbell.mm.exception.MessageException;
import com.windbell.mm.mapper.UserInfoMapper;
import com.windbell.mm.model.entities.User;
import com.windbell.mm.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    /**
     * 修改用户信息
     * @param user 用户信息
     */
    @Override
    public void saveOrUpdate(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        int i = userInfoMapper.update(user, updateWrapper);
        if (i != 1) {
            throw new MessageException(ResultCodeEnum.SERVICE_ERROR);
        }

    }
}
