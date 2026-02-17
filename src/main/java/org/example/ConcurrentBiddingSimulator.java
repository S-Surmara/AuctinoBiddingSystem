package org.example.Simulator;

import org.example.Entity.Auction;
import org.example.Entity.Bid;
import org.example.Entity.User;
import org.example.Manager.AuctionManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentBiddingSimulator {
    private final AuctionManager auctionManager;
    private final Random random;

    public ConcurrentBiddingSimulator(AuctionManager auctionManager) {
        this.auctionManager = auctionManager;
        this.random = new Random();
    }

    public void simulateConcurrentBids(String auctionId, List<User> users, int bidsPerUser) {
        // Get auction to determine appropriate bid range
        Auction auction = auctionManager.getAuctionById(auctionId);
        if (auction == null) {
            System.out.println("❌ Auction not found!");
            return;
        }

        int minPrice = auction.getMinPrice();
        int maxPrice = minPrice + 10000;  // Bid range: minPrice to minPrice+10000

        System.out.println("\n=== Simulating " + (users.size() * bidsPerUser) +
                " Concurrent Bids on " + auction.getName() + " ===");
        System.out.println("Bid Range: ₹" + minPrice + " - ₹" + maxPrice + "\n");

        ExecutorService executor = Executors.newFixedThreadPool(users.size());
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(users.size() * bidsPerUser);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (User user : users) {
            for (int i = 0; i < bidsPerUser; i++) {
                final int bidAmount = minPrice + random.nextInt(maxPrice - minPrice);

                executor.submit(() -> {
                    try {
                        startLatch.await(); // Wait for all threads to be ready

                        Thread.sleep(random.nextInt(100)); // Small random delay

                        Bid bid = new Bid(user.getUserId(), auctionId, bidAmount, LocalDateTime.now());
                        boolean success = auctionManager.placeBidforAuction(auctionId, bid);

                        if (success) {
                            successCount.incrementAndGet();
                            System.out.println("✓ [" + user.getName() + "] Bid ₹" + bidAmount + " - SUCCESS");
                        } else {
                            failCount.incrementAndGet();
                            System.out.println("✗ [" + user.getName() + "] Bid ₹" + bidAmount + " - FAILED");
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        endLatch.countDown();
                    }
                });
            }
        }

        // Start all threads simultaneously
        startLatch.countDown();

        try {
            endLatch.await(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();

        System.out.println("\n📊 Concurrent Bidding Results:");
        System.out.println("  Total Attempts: " + (users.size() * bidsPerUser));
        System.out.println("  Successful Bids: " + successCount.get());
        System.out.println("  Failed Bids: " + failCount.get());

        if (auction.getHighestBid() != null) {
            System.out.println("  Current Highest Bid: ₹" + auction.getHighestBid().getPrice());
        }
    }
}
