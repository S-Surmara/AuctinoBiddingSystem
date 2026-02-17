package org.example;

import org.example.Entity.*;
import org.example.Enums.AuctionCategory;
import org.example.Manager.AuctionManager;
import org.example.Observer.BidderObserver;
import org.example.Persistance.UserRepositry;
import org.example.scheduler.AuctionScheduler;
import org.example.Simulator.ConcurrentBiddingSimulator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OnlineAuctionSystem {

    private final AuctionManager auctionManager;
    private final AuctionScheduler auctionScheduler;
    private final UserRepositry userRepository;
    private final ConcurrentBiddingSimulator biddingSimulator;

    public OnlineAuctionSystem() {
        this.auctionManager = new AuctionManager();
        this.userRepository = new SimpleUserRepository();
        this.auctionScheduler = new AuctionScheduler(auctionManager);
        this.biddingSimulator = new ConcurrentBiddingSimulator(auctionManager);
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("   Online Auction System Started");
        System.out.println("   (Thread-Safe & Concurrent)");
        System.out.println("========================================\n");

        auctionScheduler.start();
        setupSampleData();
    }

    private void setupSampleData() {
        System.out.println("=== Setting Up Sample Data ===\n");

        // Create users
        User user1 = new User("U001", "Alice");
        User user2 = new User("U002", "Bob");
        User user3 = new User("U003", "Charlie");
        User user4 = new User("U004", "Diana");
        User user5 = new User("U005", "Eve");

        userRepository.saveUser(user1);
        userRepository.saveUser(user2);
        userRepository.saveUser(user3);
        userRepository.saveUser(user4);
        userRepository.saveUser(user5);

        System.out.println("✓ Created 5 users: Alice, Bob, Charlie, Diana, Eve\n");

        // Create auctions - FIXED: Start immediately
        LocalDateTime now = LocalDateTime.now();

        Auction auction1 = new Auction(
                "Vintage Bike",
                "Classic 1980s mountain bike",
                5000,
                now.minusSeconds(1),  // FIXED: Start 1 second ago (immediately active)
                2,  // 2 minutes duration
                AuctionCategory.BIKE
        );

        Auction auction2 = new Auction(
                "Gaming Laptop",
                "High-end gaming laptop with RTX 4090",
                50000,
                now.plusSeconds(30),  // Starts in 30 seconds
                2,
                AuctionCategory.ELECTRONICS
        );

        auctionManager.addAuction(auction1);
        auctionManager.addAuction(auction2);

        // Register observers
        auction1.addObserver(new BidderObserver(user1.getUserId(), "Alice"));
        auction1.addObserver(new BidderObserver(user2.getUserId(), "Bob"));
        auction1.addObserver(new BidderObserver(user3.getUserId(), "Charlie"));

        System.out.println("✓ Created 2 auctions with observers\n");
    }

    public void runConcurrentDemo() throws InterruptedException {
        System.out.println("=== Starting Concurrent Auction Demo ===\n");

        // Wait for scheduler to start first auction
        Thread.sleep(2000);

        List<Auction> allAuctions = auctionManager.getAllAuctions();
        Auction auction1 = allAuctions.get(0);  // Vintage Bike

        // Demo 1: Sequential bidding
        System.out.println("\n--- Demo 1: Sequential Bidding ---");
        placeBid(auction1.getAuctionId(), "U001", 6000);
        Thread.sleep(500);
        placeBid(auction1.getAuctionId(), "U002", 7000);
        Thread.sleep(500);
        placeBid(auction1.getAuctionId(), "U003", 8000);
        Thread.sleep(1000);

        // Demo 2: Concurrent bidding from multiple threads
        System.out.println("\n--- Demo 2: Concurrent Bidding (Multiple Threads) ---");
        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> placeBid(auction1.getAuctionId(), "U001", 9000));
        executor.submit(() -> placeBid(auction1.getAuctionId(), "U002", 9500));
        executor.submit(() -> placeBid(auction1.getAuctionId(), "U003", 9200));

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        Thread.sleep(2000);

        // Demo 3: Stress test with simulator - FIXED: Use correct auction
        System.out.println("\n--- Demo 3: Concurrent Stress Test ---");
        List<User> users = new ArrayList<>();
        users.add(userRepository.getUserById("U001"));
        users.add(userRepository.getUserById("U002"));
        users.add(userRepository.getUserById("U003"));
        users.add(userRepository.getUserById("U004"));
        users.add(userRepository.getUserById("U005"));

        // FIXED: Bid on auction1 (Vintage Bike), not auction2 (Gaming Laptop)
        biddingSimulator.simulateConcurrentBids(auction1.getAuctionId(), users, 3);

        // Wait for auctions to end
        System.out.println("\n--- Waiting for auctions to end... ---");
        Thread.sleep(60000);  // Wait 1 minute
    }

    private void placeBid(String auctionId, String userId, int price) {
        User user = userRepository.getUserById(userId);
        System.out.println("💰 " + user.getName() + " attempting bid: ₹" + price);

        Bid bid = new Bid(userId, auctionId, price, LocalDateTime.now());
        auctionManager.placeBidforAuction(auctionId, bid);
    }

    public void stop() {
        auctionScheduler.stop();
        System.out.println("\n========================================");
        System.out.println("   Auction System Stopped");
        System.out.println("========================================");
    }

    public static void main(String[] args) {
        OnlineAuctionSystem system = new OnlineAuctionSystem();

        try {
            system.start();
            system.runConcurrentDemo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            system.stop();
        }
    }
}
