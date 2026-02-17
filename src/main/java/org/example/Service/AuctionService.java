package org.example.Service;

import org.example.Entity.Auction;
import org.example.Entity.Bid;
import org.example.Entity.SearchObject;
import org.example.Enums.AuctionCategory;
import org.example.Manager.AuctionManager;
import org.example.Persistance.UserRepositry;
import org.example.Stratergy.AuctionSearchStratergy;

import java.time.LocalDateTime;
import java.util.List;

public class AuctionService {
    AuctionManager auctionManager;
    AuctionSearchStratergy auctionSearchStratergy;
    UserRepositry userRepositry;
    public AuctionService(AuctionManager auctionManager,AuctionSearchStratergy auctionSearchStratergy,UserRepositry userRepositry){
        this.auctionManager = auctionManager;
        this.auctionSearchStratergy = auctionSearchStratergy;
        this.userRepositry = userRepositry;
    }
    public void createAuction(String name, String description, int minPrice, LocalDateTime startTime, int duration){
        Auction auction = new Auction(name,description,minPrice,startTime,duration, AuctionCategory.BIKE);
        auctionManager.addAuction(auction);
    }

    public List<Auction> listAuctions(SearchObject searchObject){
        return auctionSearchStratergy.search(searchObject);
    }

    public void createBid(String auctionId,String userId,int price){
        Bid bid = new Bid(auctionId,userId,price,LocalDateTime.now());
        auctionManager.placeBidforAuction(auctionId,bid);
    }

    public void registerForAuction(String auctionId,String userId){
        Auction auction = auctionManager.getAuctionById(auctionId);
        auction.addObserver(userRepositry.getUserById(userId));
    }
}
