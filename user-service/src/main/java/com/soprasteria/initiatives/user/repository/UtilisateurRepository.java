package com.soprasteria.initiatives.user.repository;


import com.soprasteria.initiatives.user.domain.Utilisateur;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository des {@link Utilisateur}
 *
 * @author rjansem
 */
public interface UtilisateurRepository extends PagingAndSortingRepository<Utilisateur, Long> {

}
