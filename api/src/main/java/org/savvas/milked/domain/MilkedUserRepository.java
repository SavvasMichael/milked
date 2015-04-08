package org.savvas.milked.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilkedUserRepository extends CrudRepository<MilkedUser, Long> {
    List<MilkedUser> findByEmail(String email);

    MilkedUser findByUuid(String uuid);
}
