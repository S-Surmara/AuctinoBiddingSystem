package org.example.Observer;

import org.example.Entity.Auction;

public class BidderObserver implements AuctionObserver {
    private String userId;
    private String userName;

    public BidderObserver(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public void notifyMe(Auction auction) {
        if (auction.getHighestBid() != null) {
            if (auction.getHighestBid().getUserId().equals(userId)) {
                System.out.println("  ✓ [" + userName + "] You are now the highest bidder!");
            } else {
                System.out.println("  ⚠ [" + userName + "] You've been outbid! Current highest: ₹" +
                        auction.getHighestBid().getPrice());
            }
        }
    }
}
