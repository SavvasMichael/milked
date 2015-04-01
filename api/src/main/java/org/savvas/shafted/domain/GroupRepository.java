package org.savvas.shafted.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<ShaftGroup, Long> {
    ShaftGroup findById(Long id);
    ShaftGroup findByUserId(Long id);
}
