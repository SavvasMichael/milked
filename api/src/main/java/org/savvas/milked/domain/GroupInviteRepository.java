package org.savvas.milked.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupInviteRepository extends CrudRepository<GroupInvite, Long> {
}
