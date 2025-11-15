package com.chinna.librarymanagement.model;

import java.time.LocalDate;


import jakarta.validation.constraints.NotNull;

public class Membership { 
	
	private long memberShipId;

	
	@NotNull(message = "Membership type is required")
    private MembershipType membershipType;

    @NotNull
    private LocalDate membershipStartDate;

    @NotNull
    private LocalDate membershipExpiryDate;

    @NotNull
    private MembershipStatus status; // Shorter name: "status" instead of "memStatus"
    
    private int maxBooksAllowedToBeBorrowed;
 
    private int noOfDaysAllowed;
    // Constructor
    public Membership(@NotNull MembershipType membershipType, 
                     @NotNull LocalDate membershipStartDate,
                     @NotNull LocalDate membershipExpiryDate) {
        this.membershipType = membershipType;
        this.membershipStartDate = membershipStartDate;
        this.membershipExpiryDate = membershipExpiryDate;
        this.status = MembershipStatus.ACTIVE;
    }

    // Enums
    public enum MembershipStatus {
        ACTIVE, SUSPENDED, EXPIRED, CANCELLED
    }

    public enum MembershipType {
        REGULAR, PREMIUM, ELITE,OCCASIONAL
    }

    // Getters
    public MembershipType getMembershipType() {
        return membershipType;
    }

    public LocalDate getMembershipStartDate() {
        return membershipStartDate;
    }

    public LocalDate getMembershipExpiryDate() {
        return membershipExpiryDate;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    // Setters
    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public void setMembershipExpiryDate(LocalDate membershipExpiryDate) {
        this.membershipExpiryDate = membershipExpiryDate;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }
    
    public int getMaxBooksAllowedToBeBorrowed() {
 		return maxBooksAllowedToBeBorrowed;
 	}

 	public int getNoOfDaysAllowed() {
 		return noOfDaysAllowed;
 	}

 	public void setMaxBooksAllowedToBeBorrowed(int maxBooksAllowedToBeBorrowed) {
 		this.maxBooksAllowedToBeBorrowed = maxBooksAllowedToBeBorrowed;
 	}

 	public void setNoOfDaysAllowed(int noOfDaysAllowed) {
 		this.noOfDaysAllowed = noOfDaysAllowed;
 	}
 	
 	public long getMemberShipId() {
		return memberShipId;
	}


    
    @Override
	public String toString() {
		return "Membership [membershipType=" + membershipType + ", status=" + status + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(this==obj) return true;
		if(obj==null || getClass()!=obj.getClass())return false;
		Membership membership=(Membership)obj;
		return this.memberShipId==membership.getMemberShipId();
	}

	// Utility method - Check if membership is valid
    public boolean isValid() {
        LocalDate today = LocalDate.now();
        return status == MembershipStatus.ACTIVE && 
               (membershipExpiryDate.isAfter(today) || membershipExpiryDate.isEqual(today));
    }
    
    // Check if membership is expired
    public boolean isExpired() {
        return membershipExpiryDate.isBefore(LocalDate.now());
    }
    
    
    
}