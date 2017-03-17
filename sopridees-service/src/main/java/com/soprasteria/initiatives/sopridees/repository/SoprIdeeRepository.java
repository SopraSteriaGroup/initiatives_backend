package com.soprasteria.initiatives.sopridees.repository;

import com.soprasteria.initiatives.sopridees.domain.SoprIdee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository des {@link SoprIdee}
 * @author rjansem
 */
public interface SoprIdeeRepository extends PagingAndSortingRepository<SoprIdee, Long> {

}
