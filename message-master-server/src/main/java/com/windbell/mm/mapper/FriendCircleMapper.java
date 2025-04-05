package com.windbell.mm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.windbell.mm.model.entities.FriendsCircle;

public interface FriendCircleMapper extends BaseMapper<FriendsCircle> {

    IPage<FriendsCircle> getDynamicList(Page<FriendsCircle> page, FriendsCircle friendsCircle);

    int publish(FriendsCircle friendCircle);


}
