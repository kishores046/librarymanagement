package com.chinna.librarymanagement.repository.impls.test;

import com.chinna.librarymanagement.model.BorrowRecord;
import com.chinna.librarymanagement.repository.interfaces.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("test")
public class InMemoryBorrowRecordRepository implements BorrowRecordRepository {
    private final Map<Long, BorrowRecord> store;
    private final AtomicLong seq=new AtomicLong(0L);
    @Autowired
    public InMemoryBorrowRecordRepository(Map<Long,BorrowRecord> store){
        this.store=store;
    }
    @Override
    public BorrowRecord save(BorrowRecord br) {
        if(br.getBorrowId()==0)br.setBorrowId(seq.getAndIncrement());
        store.put(br.getBorrowId(), br);
        return br;
    }

    @Override
    public Optional<BorrowRecord> findById(long borrowId) {
        return Optional.ofNullable(store.get(borrowId));
    }

    @Override
    public List<BorrowRecord> findActiveByCustomer(long customerId) {
       List<BorrowRecord> activeBorrowRecordsOfCustomer=new ArrayList<>();
       for(BorrowRecord br:store.values()){
           if(br.getCustomerId()==customerId && !br.isReturned())activeBorrowRecordsOfCustomer.add(br);
       }
       return activeBorrowRecordsOfCustomer;
    }

    @Override
    public List<BorrowRecord> findActiveByBook(long bookId) {
        List<BorrowRecord> activeBorrowRecordsByBook=new ArrayList<>();
        for(BorrowRecord br:store.values()){
            if (br.getBookId()==bookId && !br.isReturned()){
                activeBorrowRecordsByBook.add(br);
            }
        }
        return activeBorrowRecordsByBook;
    }

    @Override
    public void update(BorrowRecord br) {
          store.put(br.getBorrowId(),br);
    }
}
