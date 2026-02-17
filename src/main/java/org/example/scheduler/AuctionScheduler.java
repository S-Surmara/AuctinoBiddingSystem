package org.example.scheduler;

import org.example.Entity.Auction;
import org.example.Entity.Bid;
import org.example.Enums.AuctionStatus;
import org.example.Manager.AuctionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuctionScheduler {
    private final AuctionManager auctionManager;
    private final ScheduledExecutorService scheduler;
    private final ReentrantReadWriteLock lock;
    private volatile boolean isRunning;

    public AuctionScheduler(AuctionManager auctionManager) {
        this.auctionManager = auctionManager;
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.lock = new ReentrantReadWriteLock();
        this.isRunning = false;
    }

    public void start() {
        lock.writeLock().lock();
        try {
            if (!isRunning) {
                isRunning = true;
                scheduler.scheduleAtFixedRate(this::checkAuctions, 0, 1, TimeUnit.SECONDS);
                System.out.println("✓ Auction Scheduler started - checking every 1 second\n");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void checkAuctions() {
        lock.readLock().lock();
        try {
            if (!isRunning) return;

            LocalDateTime now = LocalDateTime.now();
            List<Auction> allAuctions = auctionManager.getAllAuctions();

            for (Auction auction : allAuctions) {
                processAuctionState(auction, now);
            }
        } catch (Exception e) {
            System.err.println("Error in auction scheduler: " + e.getMessage());
        } finally {
            lock.readLock().unlock();
        }
    }

    private synchronized void processAuctionState(Auction auction, LocalDateTime now) {
        AuctionStatus currentStatus = auction.getAuctionStatus();

        // Start scheduled auctions
        if (currentStatus == AuctionStatus.SCHEDULED) {
            if (!now.isBefore(auction.getStartTime())) {
                auction.setAuctionStatus(AuctionStatus.ACTIVE);  // ADD THIS LINE!
                auctionManager.updateAuctionStatus(auction.getAuctionId(), AuctionStatus.ACTIVE);
                System.out.println("🟢 Auction STARTED: " + auction.getName() +
                        " (ID: " + auction.getAuctionId() + ")");
            }
        }

        // End active auctions
        if (currentStatus == AuctionStatus.ACTIVE) {
            LocalDateTime endTime = auction.getEndTime();
            if (!now.isBefore(endTime)) {
                auction.setAuctionStatus(AuctionStatus.ENDED);  // ADD THIS LINE!
                auctionManager.updateAuctionStatus(auction.getAuctionId(), AuctionStatus.ENDED);
                declareWinner(auction);
            }
        }
    }


    private synchronized void declareWinner(Auction auction) {
        System.out.println("\n🔴 Auction ENDED: " + auction.getName());

        Bid highestBid = auction.getHighestBid();
        if (highestBid != null) {
            auction.setWinnerId(highestBid.getUserId());
            System.out.println("🏆 WINNER: User " + highestBid.getUserId());
            System.out.println("💰 Winning Bid: ₹" + highestBid.getPrice());

            List<Bid> history = auction.getBidHistory();
            System.out.println("📊 Total Bids: " + history.size());
        } else {
            System.out.println("❌ No bids placed - Auction ended without winner");
        }
        System.out.println();
    }

    public void stop() {
        lock.writeLock().lock();
        try {
            isRunning = false;
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            System.out.println("✓ Auction Scheduler stopped");
        } finally {
            lock.writeLock().unlock();
        }
    }
}
