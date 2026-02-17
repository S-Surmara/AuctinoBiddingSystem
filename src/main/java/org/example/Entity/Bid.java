package org.example.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Bid {
    String bidId;
    String userId;
    String auctionId;
    int price;
    LocalDateTime timeStamp;

    public Bid(String userId,String auctionId,int price,LocalDateTime timeStamp){
        this.bidId = UUID.randomUUID().toString();
        this.userId = userId;
        this.auctionId = auctionId;
        this.price = price;
        this.timeStamp = timeStamp;
    }

    public String getBidId() {
        return bidId;
    }

    public String getUserId() {
        return userId;
    }


    public String getAuctionId() {
        return auctionId;
    }

    public int getPrice() {
        return price;
    }


    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
