package org.savvas.milked.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MilkedUserRepository extends CrudRepository<MilkedUser, Long> {
    MilkedUser findByEmail(String email);
    MilkedUser findByUuid(String uuid);
}
