package org.example.Observer;

import org.example.Entity.Auction;

public interface AuctionObserver {
    public void notifyMe(Auction auction);
}
