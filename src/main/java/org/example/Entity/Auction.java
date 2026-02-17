package org.example.Entity;

import org.example.Enums.AuctionCategory;
import org.example.Enums.AuctionStatus;
import org.example.Observer.AuctionObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Auction {
    private final String auctionId;
    private final String name;
    private final String description;
    private final int minPrice;
    private final LocalDateTime startTime;
    private final int duration;
    private final AuctionCategory auctionCategory;

    // Thread-safe collections
    private final List<AuctionObserver> auctionObserverList;
    private final List<Bid> bidHistory;

    // Mutable fields with synchronized access
    private volatile Bid highestBid;
    private volatile AuctionStatus auctionStatus;
    private volatile String winnerId;

    public Auction(String name, String description, int minPrice, LocalDateTime startTime,
                   int duration, AuctionCategory auctionCategory){
        this.auctionId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.minPrice = minPrice;
        this.startTime = startTime;
        this.duration = duration;
        this.auctionStatus = AuctionStatus.SCHEDULED;
        this.auctionCategory = auctionCategory;

        // Thread-safe collections
        this.auctionObserverList = new CopyOnWriteArrayList<>();
        this.bidHistory = new CopyOnWriteArrayList<>();
    }

    // Getters
    public String getAuctionId() {
        return auctionId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public AuctionCategory getAuctionCategory() {
        return auctionCategory;
    }

    // Thread-safe observer management
    public synchronized void addObserver(AuctionObserver auctionObserver){
        if (auctionObserver != null) {
            auctionObserverList.add(auctionObserver);
        }
    }

    public synchronized void removeObserver(AuctionObserver auctionObserver){
        auctionObserverList.remove(auctionObserver);
    }

    // Thread-safe notification
    public void notifyObservers(){
        // CopyOnWriteArrayList is thread-safe for iteration
        for (AuctionObserver observer : auctionObserverList) {
            try {
                observer.notifyMe(this);
            } catch (Exception e) {
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }

    // Thread-safe bid management
    public synchronized void setHighestBid(Bid highestBid){
        this.highestBid = highestBid;
        notifyObservers();
    }

    public synchronized Bid getHighestBid(){
        return this.highestBid;
    }

    public synchronized void addBidToHistory(Bid bid){
        bidHistory.add(bid);
    }

    public synchronized List<Bid> getBidHistory(){
        return new ArrayList<>(bidHistory);
    }

    // Thread-safe status management
    public synchronized AuctionStatus getAuctionStatus(){
        return this.auctionStatus;
    }

    public synchronized void setAuctionStatus(AuctionStatus auctionStatus){
        this.auctionStatus = auctionStatus;
    }

    // Thread-safe winner management
    public synchronized String getWinnerId(){
        return this.winnerId;
    }

    public synchronized void setWinnerId(String winnerId){
        this.winnerId = winnerId;
    }

    // Thread-safe auction state check
    public synchronized boolean isActive() {
        return auctionStatus == AuctionStatus.ACTIVE &&
                LocalDateTime.now().isBefore(getEndTime());
    }
}
