package com.marketplace.service.impl;

import com.marketplace.dto.FollowerDTO;
import com.marketplace.exception.AlreadyFollowingException;
import com.marketplace.exception.FollowerNotFoundException;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.mapper.FollowerMapper;
import com.marketplace.model.Buyer;
import com.marketplace.model.Follower;
import com.marketplace.model.Seller;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.FollowerRepository;
import com.marketplace.repository.SellerRepository;
import com.marketplace.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class FollowerServiceImpl implements FollowerService {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private FollowerMapper followerMapper;

    @Override
    @Transactional
    public FollowerDTO follow(Long buyerId, Long sellerId) {
        if (isFollowing(buyerId, sellerId)) {
            throw new AlreadyFollowingException(buyerId.toString(), sellerId.toString());
        }

        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "id", buyerId.toString()));
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "id", sellerId.toString()));

        Follower follower = new Follower();
        follower.setBuyer(buyer);
        follower.setSeller(seller);

        Follower savedFollower = followerRepository.save(follower);
        return followerMapper.toDto(savedFollower);
    }

    @Override
    @Transactional
    public void unfollow(Long buyerId, Long sellerId) {
        Follower follower = followerRepository.findByBuyerIdAndSellerId(buyerId, sellerId)
                .orElseThrow(() -> new FollowerNotFoundException(buyerId.toString(), sellerId.toString()));
        followerRepository.delete(follower);
    }

    @Override
    public List<FollowerDTO> getAllFollowersOfSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "id", sellerId.toString()));

        List<Follower> followers = followerRepository.findBySellerId(sellerId);
        return followers.stream()
                .map(followerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Buyer> getAllFollowersOfSellerTest(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "id", sellerId.toString()));

        List<Follower> followers = followerRepository.findBySellerId(sellerId);
        return followers.stream()
                .map(Follower::getBuyer)
                .collect(Collectors.toList());
    }

    @Override
    public List<FollowerDTO> getAllFollowingForBuyer(Long buyerId) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "id", buyerId.toString()));

        List<Follower> following = followerRepository.findByBuyerId(buyerId);
        return following.stream()
                .map(followerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFollowing(Long buyerId, Long sellerId) {
        return followerRepository.existsByBuyerIdAndSellerId(buyerId, sellerId);
    }

    @Override
    public FollowerDTO getFollowerById(Long id) {
        Follower follower = followerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follower", "id", id.toString()));
        return followerMapper.toDto(follower);
    }

    @Override
    public List<FollowerDTO> getAllFollowers() {
        List<Follower> followers = followerRepository.findAll();
        return followers.stream()
                .map(followerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getFollowerCountForSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "id", sellerId.toString()));

        return followerRepository.countBySellerId(sellerId);

    }
}