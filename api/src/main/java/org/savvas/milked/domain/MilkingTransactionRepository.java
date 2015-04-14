package org.savvas.milked.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilkingTransactionRepository extends CrudRepository<MilkingTransaction, Long> {
    List<MilkingTransaction> findByMilkingGroupId(Long id);
}
