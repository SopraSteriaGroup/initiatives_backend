package com.soprasteria.initiatives.auth.repository;

import com.soprasteria.initiatives.auth.domain.IdentifiableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Generic Spring Data JPA repository {@link IdentifiableEntity}
 *
 * @author jntakpe
 */
public interface GenericRepository<T extends IdentifiableEntity> extends JpaRepository<T, Long> {

}
