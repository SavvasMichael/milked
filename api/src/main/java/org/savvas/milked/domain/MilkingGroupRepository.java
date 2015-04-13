package org.savvas.milked.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilkingGroupRepository extends CrudRepository<MilkingGroup, Long> {
    MilkingGroup findById(Long id);

    @Query("select m from MilkingGroup m inner join m.milkedUsers u where u = :milkedUser")
    List<MilkingGroup> findGroupsContianingUserId(@Param("milkedUser") MilkedUser milkedUser);
}
