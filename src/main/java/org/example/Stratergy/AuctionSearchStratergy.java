package org.example.Stratergy;

import org.example.Entity.Auction;
import org.example.Entity.SearchObject;

import java.util.List;

public interface AuctionSearchStratergy {
    List<Auction> search(SearchObject searchObject);
}
