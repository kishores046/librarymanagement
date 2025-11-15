package com.chinna.librarymanagement.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinna.librarymanagement.model.Membership;
import com.chinna.librarymanagement.model.factory.MembershipFactory;
@Service
public class MembershipService {
    private final MembershipFactory membershipFactory;

    public MembershipService(MembershipFactory membershipFactory) {
		this.membershipFactory = membershipFactory;
	}
 
    public Membership generateMembership(String type,LocalDate startTime) {
    	return membershipFactory.generateMembership(type.toUpperCase(),startTime);
    }

	
}