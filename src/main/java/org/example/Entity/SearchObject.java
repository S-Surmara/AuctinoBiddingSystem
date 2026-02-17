package org.example.Entity;

import org.example.Enums.AuctionCategory;

import java.time.LocalDateTime;

public class SearchObject {
    private AuctionCategory auctionCategory;
    private int price;

    private LocalDateTime startTime;

    public static class Builder{
        SearchObject searchObject;
        public Builder(){
            searchObject = new SearchObject();
        }

        public Builder withAuctionCategory(AuctionCategory auctionCategory){
            searchObject.auctionCategory = auctionCategory;
            return this;
        }

        public Builder withStartTime(LocalDateTime startTime){
            searchObject.startTime = startTime;
            return this;
        }

        public Builder withPrice(int price){
            searchObject.price = price;
            return this;
        }

        public SearchObject build(){
            return searchObject;
        }
    }

    public AuctionCategory getAuctionCategory() {
        return auctionCategory;
    }

    public int getPrice() {
        return price;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
