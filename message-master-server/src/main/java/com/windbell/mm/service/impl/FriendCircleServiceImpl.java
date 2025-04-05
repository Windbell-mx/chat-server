package com.windbell.mm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windbell.mm.enums.ResultCodeEnum;
import com.windbell.mm.exception.MessageException;
import com.windbell.mm.mapper.FriendsCircleLikeRecordMapper;
import com.windbell.mm.mapper.FriendCircleMapper;
import com.windbell.mm.model.entities.FriendsCircleLikeRecord;
import com.windbell.mm.model.entities.FriendsCircle;
import com.windbell.mm.redis.RedisService;
import com.windbell.mm.service.FriendCircleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FriendCircleServiceImpl implements FriendCircleService {

    private final FriendCircleMapper friendCircleMapper;
    private final FriendsCircleLikeRecordMapper friendCircleLikeRecordMapper;
    private final RedisService redisService;

    /**
     * 获取全部动态
     * @param page 页数
     * @param friendsCircle 动态对象实例
     * @return 返回结果集
     */
    @Override
    public IPage<FriendsCircle> getDynamicList(Page<FriendsCircle> page,FriendsCircle friendsCircle) {
        return friendCircleMapper.getDynamicList(page,friendsCircle);
    }

    /**
     * 根据用户名查询动态
     * @param account 用户名
     * @return 返回结果集
     */
    @Override
    public List<FriendsCircle> getDynamicByAccount(String account) {
        FriendsCircle[] array = redisService.get("getDynamicByAccount", FriendsCircle[].class);
        List<FriendsCircle> list = array != null ? Arrays.asList(array) : new ArrayList<>();
        if (list.isEmpty()) {
            LambdaQueryWrapper<FriendsCircle> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FriendsCircle::getAccount,account);
            list = friendCircleMapper.selectList(queryWrapper);
            redisService.set("getDynamicByAccount",list,3600);
        }
        return list;
    }

    /**
     * 朋友圈点赞
     * @param account 点赞用户
     * @param titleId 动态唯一ID
     * @param like 点赞状态
     * @param comment 评论
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeAndComment(String account,String titleId,int like,String comment) {
        System.out.println(account);
        FriendsCircle friendsCircle = friendCircleMapper.selectById(titleId);
        if (friendsCircle == null) {
            throw new MessageException(ResultCodeEnum.DATA_ERROR);
        }
        FriendsCircleLikeRecord friendCircleLikeRecord = new FriendsCircleLikeRecord();
        friendCircleLikeRecord.setAccount(account);
        friendCircleLikeRecord.setPublishedByAccount(friendsCircle.getAccount());
        friendCircleLikeRecord.setTitleId(friendsCircle.getTitleId());
        friendCircleLikeRecord.setContext(friendsCircle.getContext());
        friendCircleLikeRecord.setMy_like(like);
        friendCircleLikeRecord.setMyComment(comment);

        int i = friendCircleLikeRecordMapper.insert(friendCircleLikeRecord);
        if (i != 1) {
            throw new MessageException(ResultCodeEnum.SERVICE_ERROR);
        }
    }

    /**
     * 发布动态
     * @param friendCircle 动态对象实例
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(FriendsCircle friendCircle) {
        int i = friendCircleMapper.publish(friendCircle);
        if (i != 1) {
            throw new MessageException(ResultCodeEnum.SERVICE_ERROR);
        }
    }

    /**
     * 朋友动态点赞评论修改
     * @param record 被修改动态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(FriendsCircleLikeRecord record) {
        LambdaUpdateWrapper<FriendsCircleLikeRecord> updateWrapper = new LambdaUpdateWrapper<>();
        int myLike = record.getMy_like();
        String myComment = record.getMyComment();
        //如果点赞状态取消，评论删除，则删除朋友圈数据
        if (myLike == 0 && myComment == null) {
            int i = friendCircleLikeRecordMapper.deleteById(record.getTitleId());
            if (i != 1) {
                throw new MessageException(ResultCodeEnum.SERVICE_ERROR);
            }
        }
        //如果点赞状态取消
        if (myLike == 0) {
            updateWrapper.set(FriendsCircleLikeRecord::getMy_like, myLike);
            int update = friendCircleLikeRecordMapper.update(updateWrapper);
            if (update != 1) {
                throw new MessageException(ResultCodeEnum.SERVICE_ERROR);
            }
        }
        //如果评论删除
        if (myComment == null) {
            updateWrapper.set(FriendsCircleLikeRecord::getMyComment, myComment);
            int update = friendCircleLikeRecordMapper.update(updateWrapper);
            if (update != 1) {
                throw new MessageException(ResultCodeEnum.SERVICE_ERROR);
            }
        }
    }
}
