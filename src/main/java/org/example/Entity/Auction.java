package org.example.Entity;

import org.example.Enums.AuctionCategory;
import org.example.Enums.AuctionStatus;
import org.example.Observer.AuctionObserver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Auction {
    private String auctionId;
    private String name;
    private String description;
    private int minPrice;
    private LocalDateTime startTime;
    private int duration;
    private AuctionCategory auctionCategory;
    private List<AuctionObserver> auctionObserverList;

    private Bid highestBid;

    AuctionStatus auctionStatus;

    public Auction(String name, String description, int minPrice, LocalDateTime startTime, int duration, AuctionCategory auctionCategory){
        this.auctionId = UUID.randomUUID().toString();
        this.name = name;
        this.description =description;
        this.minPrice = minPrice;
        this.startTime = startTime;
        this.duration = duration;
        this.auctionStatus = AuctionStatus.SCHEDULED;
        this.auctionCategory = auctionCategory;
    }

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

    public void addObserver(AuctionObserver auctionObserver){
        auctionObserverList.add(auctionObserver);
    }

    public void notifyObservers(){
        auctionObserverList.forEach((observer) -> observer.notifyMe(this));
    }

    public void setHighestBid(Bid highestBid){
        this.highestBid = highestBid;
        notifyObservers();
    }

    public Bid getHighestBid(){
        return this.highestBid;
    }

    public AuctionStatus getAuctionStatus(){
        return this.auctionStatus;
    }

    public void setAuctionStatus(AuctionStatus auctionStatus){
        this.auctionStatus = auctionStatus;
    }

    public AuctionCategory getAuctionCategory() {
        return auctionCategory;
    }
}
