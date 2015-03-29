package org.savvas.shafted.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<ShaftUser, Long> {
    List<ShaftUser> findByEmail(String email);

    ShaftUser findByUuid(String uuid);

    ShaftGroup findById(Long id);
}
