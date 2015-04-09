package org.savvas.milked.service;

import org.savvas.milked.controller.request.MilkingTransactionRequest;
import org.savvas.milked.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Long createMilkingTransaction(MilkingTransactionRequest request) {
        MilkedUser milker = milkedUserRepository.findOne(request.getMilkerId());
        MilkedUser milkee = milkedUserRepository.findOne(request.getMilkeeId());
        MilkingGroup group = milkingGroupRepository.findById(request.getGroupId());

        MilkingTransaction milkingTransaction = new MilkingTransaction(milker, milkee, group, request.getAmount());
        MilkingTransaction savedMilkingTransaction = milkingTransactionRepository.save(milkingTransaction);
        return savedMilkingTransaction.getId();
    }

    public MilkingTransaction getMilkingTransaction(Long id) {
        return milkingTransactionRepository.findOne(id);
    }
}
