package org.example.Manager;

import org.example.Entity.Auction;
import org.example.Entity.Bid;
import org.example.Enums.AuctionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionManager {
    private final Map<String, Auction> auctionMap;
    private final Map<String, ReentrantLock> auctionLocks;  // Per-auction locks
    private final ReentrantLock managerLock;

    public AuctionManager(){
        this.auctionMap = new ConcurrentHashMap<>();
        this.auctionLocks = new ConcurrentHashMap<>();
        this.managerLock = new ReentrantLock();
    }

    public void addAuction(Auction auction){
        managerLock.lock();
        try {
            auctionMap.put(auction.getAuctionId(), auction);
            auctionLocks.put(auction.getAuctionId(), new ReentrantLock());
        } finally {
            managerLock.unlock();
        }
    }

    public Auction getAuctionById(String auctionId){
        return auctionMap.get(auctionId);
    }

    public List<Auction> getAllAuctions(){
        return new ArrayList<>(auctionMap.values());
    }

    // Thread-safe bid placement with per-auction locking
    public synchronized boolean placeBidforAuction(String auctionId, Bid bid){
        ReentrantLock auctionLock = auctionLocks.get(auctionId);
        if (auctionLock == null) {
            System.out.println("❌ Auction not found: " + auctionId);
            return false;
        }

        auctionLock.lock();
        try {
            Auction auction = auctionMap.get(auctionId);

            if (auction == null) {
                System.out.println("❌ Auction not found");
                return false;
            }

            // Check auction status
            if (auction.getAuctionStatus() != AuctionStatus.ACTIVE) {
                System.out.println("❌ Auction is not active (Status: " + auction.getAuctionStatus() + ")");
                return false;
            }

            // Check if auction has ended
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endTime = auction.getStartTime().plusMinutes(auction.getDuration());
            if (!now.isBefore(endTime)) {
                System.out.println("❌ Auction has ended");
                return false;
            }

            // Validate bid amount
            if (auction.getHighestBid() == null) {
                if (bid.getPrice() < auction.getMinPrice()) {
                    System.out.println("❌ Bid must be at least ₹" + auction.getMinPrice());
                    return false;
                }
            } else {
                if (bid.getPrice() <= auction.getHighestBid().getPrice()) {
                    System.out.println("❌ Bid must be higher than current highest bid (₹" +
                            auction.getHighestBid().getPrice() + ")");
                    return false;
                }
            }

            // Place bid successfully
            auction.setHighestBid(bid);
            auction.addBidToHistory(bid);
            return true;

        } finally {
            auctionLock.unlock();
        }
    }

    // Thread-safe status update
    public synchronized void updateAuctionStatus(String auctionId, AuctionStatus newStatus) {
        ReentrantLock auctionLock = auctionLocks.get(auctionId);
        if (auctionLock != null) {
            auctionLock.lock();
            try {
                Auction auction = auctionMap.get(auctionId);
                if (auction != null) {
                    auction.setAuctionStatus(newStatus);
                }
            } finally {
                auctionLock.unlock();
            }
        }
    }
}
