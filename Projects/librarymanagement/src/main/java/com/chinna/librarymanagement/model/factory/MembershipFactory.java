package com.chinna.librarymanagement.model.factory;

import java.time.LocalDate;

import com.chinna.librarymanagement.model.Membership;
import org.springframework.stereotype.Component;

import com.chinna.librarymanagement.model.Membership.MembershipType;
@Component
public class MembershipFactory {
    public Membership generateMembership(String type,LocalDate startTime) {
        Membership membership;
        switch(type) {
		    case "REGULAR"->{
		    	membership =new Membership(MembershipType.REGULAR,startTime,startTime.plusMonths(2));
		    	membership.setMaxBooksAllowedToBeBorrowed(10);
		    	membership.setNoOfDaysAllowed(10);
		    }
		    case "ELITE"->{
		    	membership =new Membership(MembershipType.ELITE,startTime,startTime.plusMonths(6));
		    	membership.setMaxBooksAllowedToBeBorrowed(15);
		    	membership.setNoOfDaysAllowed(20);
		    }
		    case "PREMIUM"->{
		    	membership =new Membership(MembershipType.PREMIUM,startTime,startTime.plusMonths(12));
		    	membership.setMaxBooksAllowedToBeBorrowed(20);
		    	membership.setNoOfDaysAllowed(25);
		    }
		    default ->{
		    	membership =new Membership(MembershipType.OCCASIONAL,startTime,startTime.plusDays(1));
		    	membership.setMaxBooksAllowedToBeBorrowed(2);
		    	membership.setNoOfDaysAllowed(7);
		    }
		};
		return membership;
	}
}
