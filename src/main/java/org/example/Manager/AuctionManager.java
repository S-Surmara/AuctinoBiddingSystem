package org.example.Manager;

import org.example.Entity.Auction;
import org.example.Entity.Bid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionManager {
    Map<String, Auction> auctionMap;

    public AuctionManager(){
        this.auctionMap = new HashMap<>();
    }

    public void addAuction(Auction auction){
        auctionMap.put(auction.getAuctionId(),auction);
    }

    public Auction getAuctionById(String auctionId){
        return auctionMap.get(auctionId);
    }

    public List<Auction> getAllAuctions(){
        return new ArrayList<>(auctionMap.values());
    }

    public void placeBidforAuction(String auctionId, Bid bid){
        if(bid.getPrice() > auctionMap.get(auctionId).getHighestBid().getPrice()){
            auctionMap.get(auctionId).setHighestBid(bid);
        }
    }
}
