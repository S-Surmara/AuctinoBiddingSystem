package org.example.Entity;

import org.example.Observer.AuctionObserver;

import java.util.UUID;

public class User implements AuctionObserver {
    private String userId;
    private String name;

    public User(String userId, String name){
        this.userId = userId;  // FIXED: Use the passed userId, not generate new one
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
        if (auction.getHighestBid() != null) {
            System.out.println("  [" + name + "] Highest bid for Auction " +
                    auction.getName() + " is: ₹" + auction.getHighestBid().getPrice());
        }
    }
}
