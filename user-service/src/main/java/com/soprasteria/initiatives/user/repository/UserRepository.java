package com.soprasteria.initiatives.user.repository;


import com.soprasteria.initiatives.user.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Repository des {@link User}
 *
 * @author rjansem
 * @author cegiraud
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsernameAndCodeTemporaire(String username, String codeTemporaire);

    boolean existsByUsername(String username);

}
