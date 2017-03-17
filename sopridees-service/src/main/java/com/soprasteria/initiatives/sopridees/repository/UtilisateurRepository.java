package com.soprasteria.initiatives.sopridees.repository;

import com.soprasteria.initiatives.sopridees.domain.Utilisateur;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository des {@link Utilisateur}
 *
 * @author rjansem
 */
public interface UtilisateurRepository extends PagingAndSortingRepository<Utilisateur, Long> {

}
