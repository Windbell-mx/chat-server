package com.windbell.mm.service;

import com.windbell.mm.model.entities.UserRelationship;

import java.util.List;

public interface UserRelationshipService {
    List<UserRelationship> getFriendList(String friendAccount);

    void delFriend(String friendAccount);
}
