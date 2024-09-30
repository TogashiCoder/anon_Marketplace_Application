package com.marketplace.controller;

import com.marketplace.dto.FollowerDTO;
import com.marketplace.service.FollowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/followers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FollowerController {


    private final FollowerService followerService;

    @PostMapping("/follow")
    public ResponseEntity<FollowerDTO> follow(@Valid @RequestBody FollowerDTO followerDTO) {
        return new ResponseEntity<>(followerService.follow(followerDTO.getBuyerId(), followerDTO.getSellerId()), HttpStatus.CREATED);
    }

//    @DeleteMapping("/unfollow")
//    public ResponseEntity<Void> unfollow(@RequestParam Long buyerId, @RequestParam Long sellerId) {
//        followerService.unfollow(buyerId, sellerId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping("/unfollow/{buyerId}/{sellerId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long buyerId, @PathVariable Long sellerId) {
    followerService.unfollow(buyerId, sellerId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<FollowerDTO>> getAllFollowersOfSeller(@PathVariable Long sellerId) {
        return new ResponseEntity<>(followerService.getAllFollowersOfSeller(sellerId), HttpStatus.OK);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<FollowerDTO>> getAllFollowingForBuyer(@PathVariable Long buyerId) {
        return new ResponseEntity<>(followerService.getAllFollowingForBuyer(buyerId), HttpStatus.OK);
    }

//    @GetMapping("/check")
//    public ResponseEntity<Boolean> isFollowing(@RequestParam Long buyerId, @RequestParam Long sellerId) {
//        return new ResponseEntity<>(followerService.isFollowing(buyerId, sellerId), HttpStatus.OK);
//    }

    @GetMapping("/check/{buyerId}/{sellerId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long buyerId, @PathVariable Long sellerId) {
        return new ResponseEntity<>(followerService.isFollowing(buyerId, sellerId), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FollowerDTO> getFollowerById(@PathVariable Long id) {
        return new ResponseEntity<>(followerService.getFollowerById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FollowerDTO>> getAllFollowers() {
        return new ResponseEntity<>(followerService.getAllFollowers(), HttpStatus.OK);
    }

    @GetMapping("/seller/{sellerId}/count")
    public ResponseEntity<Long> getFollowerCountForSeller(@PathVariable Long sellerId) {
        return new ResponseEntity<>(followerService.getFollowerCountForSeller(sellerId), HttpStatus.OK);
    }
}