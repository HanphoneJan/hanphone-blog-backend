package com.example.blog2.service;

import com.example.blog2.po.FriendLink;

import java.util.List;

public interface FriendLinkService {
    List<FriendLink> listFriendLink();

    void deleteFriendLink(Long id);

    FriendLink saveFriendLink(FriendLink FriendLink);

    FriendLink updateFriendLink(Long id,FriendLink FriendLink);
    Boolean changeRecommend(Long friendLinkId, Boolean recommend);
}
