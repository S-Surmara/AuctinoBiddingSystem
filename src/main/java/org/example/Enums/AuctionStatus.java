package org.example.Enums;

public enum AuctionStatus {
    SCHEDULED,    // Created but not started
    ACTIVE,       // Currently accepting bids
    ENDED,        // Time expired
    CANCELLED     // Cancelled by seller
}
