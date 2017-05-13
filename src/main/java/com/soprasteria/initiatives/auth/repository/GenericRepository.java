package com.soprasteria.initiatives.auth.repository;

import com.soprasteria.initiatives.auth.domain.IdentifiableEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Generic repository
 *
 * @author jntakpe
 */
public interface GenericRepository<T extends IdentifiableEntity> extends ReactiveMongoRepository<T, String> {

}
