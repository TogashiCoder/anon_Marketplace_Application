package com.marketplace.service;

import com.marketplace.dto.FollowerDTO;
import com.marketplace.model.Buyer;

import java.util.List;

public interface FollowerService {
    FollowerDTO follow(Long buyerId, Long sellerId);
    void unfollow(Long buyerId, Long sellerId);
    List<FollowerDTO> getAllFollowersOfSeller(Long sellerId);
    List<Buyer> getAllFollowersOfSellerTest(Long sellerId);
    List<FollowerDTO> getAllFollowingForBuyer(Long buyerId);
    boolean isFollowing(Long buyerId, Long sellerId);
    FollowerDTO getFollowerById(Long id);
    List<FollowerDTO> getAllFollowers();
    long getFollowerCountForSeller(Long sellerId);
}
