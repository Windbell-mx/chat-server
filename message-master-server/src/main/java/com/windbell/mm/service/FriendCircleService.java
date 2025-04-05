package com.windbell.mm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windbell.mm.model.entities.FriendsCircle;
import com.windbell.mm.model.entities.FriendsCircleLikeRecord;

import java.util.List;


public interface FriendCircleService {
    IPage<FriendsCircle> getDynamicList(Page<FriendsCircle> page,FriendsCircle friendsCircle);

    List<FriendsCircle> getDynamicByAccount(String account);

    void likeAndComment(String account,String titleId,int like,String comment);

    void publish(FriendsCircle friendCircle);

    void cancel(FriendsCircleLikeRecord record);
}
