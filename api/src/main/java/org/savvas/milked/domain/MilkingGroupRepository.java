package org.savvas.milked.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MilkingGroupRepository extends CrudRepository<MilkingGroup, Long> {
    MilkingGroup findById(Long id);
}
