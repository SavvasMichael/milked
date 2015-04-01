package org.savvas.shafted.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShaftRepository extends CrudRepository<Shaft, Long> {
    List<Shaft> findByGroupId(Long id);
}
