package org.example.Entity;

import org.example.Observer.AuctionObserver;

import java.util.UUID;

public class User implements AuctionObserver {
    private String userId;
    private String name;

    public User(String userId,String name){
        this.userId = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    @Override
    public void notifyMe(Auction auction){
        System.out.println("highest bid for Auctoin" + auction.getAuctionId() + "is: "+ auction.getHighestBidPrice());
    }
}
