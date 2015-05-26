package org.savvas.milked.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupInviteRepository extends CrudRepository<GroupInvite, Long> {
    List<GroupInvite> findByUserId(Long id);

    GroupInvite findOneByGroupIdAndUserId(Long groupId, Long userId);

    GroupInvite findOneByGroupIdAndUuid(Long groupId, String uuid);
}
