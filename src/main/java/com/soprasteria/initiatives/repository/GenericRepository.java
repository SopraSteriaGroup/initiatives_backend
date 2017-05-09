package com.soprasteria.initiatives.repository;

import com.soprasteria.initiatives.domain.IdentifiableEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Generic repository
 *
 * @author jntakpe
 */
public interface GenericRepository<T extends IdentifiableEntity> extends ReactiveMongoRepository<T, String> {

}
