package org.example.Stratergy;

import org.example.Entity.Auction;
import org.example.Entity.SearchObject;
import org.example.Manager.AuctionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuctionSearchByCategory implements AuctionSearchStratergy{

    AuctionManager auctionManager;

    public  AuctionSearchByCategory(AuctionManager auctionManager){
        this.auctionManager = auctionManager;
    }
    @Override
    public List<Auction> search(SearchObject searchObject){
        List<Auction> auctionList = auctionManager.getAllAuctions().stream()
                    .filter((auction -> auction.getAuctionCategory() == searchObject.getAuctionCategory()))
                    .collect(Collectors.toList());
        return auctionList;
    }
}
