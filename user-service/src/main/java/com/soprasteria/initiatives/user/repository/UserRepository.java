package com.soprasteria.initiatives.user.repository;


import com.soprasteria.initiatives.user.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository des {@link User}
 *
 * @author rjansem
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

}
