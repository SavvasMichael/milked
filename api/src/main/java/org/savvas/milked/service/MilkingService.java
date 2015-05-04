package org.savvas.milked.service;

import org.savvas.milked.controller.error.NotFoundException;
import org.savvas.milked.controller.request.MilkingTransactionRequest;
import org.savvas.milked.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilkingService {

    private final MilkingTransactionRepository milkingTransactionRepository;
    private final MilkedUserRepository milkedUserRepository;
    private final MilkingGroupRepository milkingGroupRepository;
    @Autowired

    public MilkingService(MilkingTransactionRepository milkingTransactionRepository, MilkedUserRepository milkedUserRepository, MilkingGroupRepository milkingGroupRepository) {
        this.milkingTransactionRepository = milkingTransactionRepository;
        this.milkedUserRepository = milkedUserRepository;
        this.milkingGroupRepository = milkingGroupRepository;
    }

    public Long createMilkingTransaction(MilkingTransactionRequest request, Long groupId) {
        MilkingGroup group = milkingGroupRepository.findById(groupId);
        MilkedUser milker = milkedUserRepository.findOne(request.getMilkerId());
        MilkedUser milkee = milkedUserRepository.findOne(request.getMilkeeId());

        MilkingTransaction milkingTransaction = new MilkingTransaction(group.getId(), milker, milkee, request.getAmount(), request.getDescription());
        MilkingTransaction savedMilkingTransaction = milkingTransactionRepository.save(milkingTransaction);
        return savedMilkingTransaction.getId();
    }

    public List<MilkingTransaction> getMilkingTransactions(Long groupId) {
        List<MilkingTransaction> transactionsList = milkingTransactionRepository.findByMilkingGroupId(groupId);
        if (transactionsList == null) {
            throw new NotFoundException("Transaction not found");
        }
        return transactionsList;
    }
}
