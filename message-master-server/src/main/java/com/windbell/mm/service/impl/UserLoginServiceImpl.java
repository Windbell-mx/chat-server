package com.windbell.mm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.windbell.mm.enums.ResultCodeEnum;
import com.windbell.mm.exception.MessageException;
import com.windbell.mm.mapper.UserLoginMapper;
import com.windbell.mm.model.entities.User;
import com.windbell.mm.service.UserLoginService;
import com.windbell.mm.utils.JWTUtil;
import com.windbell.mm.utils.MyUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserLoginMapper userLoginMapper;

    /**
     * 用户登录验证
     * @param account 账号
     * @param password 密码
     * @return 返回值
     */
    @Override
    public String login(String account, String password) {
        if (account == null || password == null) {
            throw new MessageException(ResultCodeEnum.ACCOUNT_AND_PASSWORD_NOT_EMPTY);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, account);
        User user = userLoginMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new MessageException(ResultCodeEnum.ACCOUNT_NOT_EXIST_ERROR);
        }
        if (!user.getPassword().equals(password)) {
            throw new MessageException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        return JWTUtil.createJwtToken(account);
    }

    /**
     *  用户注册
     * @param user 用户注册信息
     */
    @Override
    public void register(User user) {
        if (user.getAccount().isEmpty() || user.getPassword().isEmpty()) {
            throw new MessageException(ResultCodeEnum.ACCOUNT_AND_PASSWORD_NOT_EMPTY);
        }
        if (user.getNickName() == null || user.getNickName().isEmpty()) {
            user.setNickName(MyUUID.create(0,10));
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, user.getAccount());
        User one = userLoginMapper.selectOne(queryWrapper);
        if (one != null) {
            throw new MessageException(ResultCodeEnum.ACCOUNT_EXIST_ERROR);
        }
        int i = userLoginMapper.insert(user);
        if (i != 1) {
            throw new MessageException(ResultCodeEnum.SERVICE_ERROR);
        }
    }
}
