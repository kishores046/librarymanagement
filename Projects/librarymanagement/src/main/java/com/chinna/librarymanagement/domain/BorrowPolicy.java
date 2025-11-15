package com.chinna.librarymanagement.domain;

import com.chinna.librarymanagement.model.Membership;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class BorrowPolicy {
    public static LocalDate computeDueDate(LocalDate borrowDate, Membership membership){
        long days=Math.max(1, membership.getMaxBooksAllowedToBeBorrowed());
        return borrowDate.plusDays(days);
    }

    public static int computeFine(LocalDate dueDate,LocalDate returnDate){
        if(returnDate==null || !returnDate.isAfter(dueDate))return  0;
        long days= ChronoUnit.DAYS.between(returnDate,dueDate);
        if(days<=7)return (int)days*5;
        else return (int)(7 * 5 + (days - 7) * 10);
    }
}
