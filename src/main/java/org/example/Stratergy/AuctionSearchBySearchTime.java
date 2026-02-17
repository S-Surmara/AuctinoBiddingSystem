package org.example.Stratergy;

import org.example.Entity.Auction;
import org.example.Entity.SearchObject;
import org.example.Manager.AuctionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuctionSearchBySearchTime implements AuctionSearchStratergy{
    AuctionManager auctionManager;
    public  AuctionSearchBySearchTime(AuctionManager auctionManager){
        this.auctionManager = auctionManager;
    }
    @Override
    public List<Auction> search(SearchObject searchObject){
        return auctionManager.getAllAuctions().stream()
                .filter(auction -> auction.getStartTime().isBefore(searchObject.getStartTime()) ||
                        auction.getStartTime().isEqual(searchObject.getStartTime()))
                .collect(Collectors.toList());
    }

}
