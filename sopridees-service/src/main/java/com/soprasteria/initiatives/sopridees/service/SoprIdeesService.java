package com.soprasteria.initiatives.sopridees.service;

import com.soprasteria.initiatives.sopridees.domain.SoprIdee;
import com.soprasteria.initiatives.sopridees.repository.SoprIdeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Services associés à la gestion des SoprIdees
 *
 * @author rjansem
 */
@Service
public class SoprIdeesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoprIdeesService.class);

    private final SoprIdeeRepository soprIdeeRepository;

    @Autowired
    public SoprIdeesService(SoprIdeeRepository soprIdeeRepository) {
        this.soprIdeeRepository = soprIdeeRepository;
    }

    /**
     * Récupère une page de {@link SoprIdee}
     *
     * @param page information de pagination
     * @return une page de {@link SoprIdee}
     */
    public Page findAll(Pageable page) {
        return soprIdeeRepository.findAll(page);
    }

    /**
     * Crée une {@link SoprIdee}
     *
     * @param soprIdee {@link SoprIdee} à créer
     */
    public void create(SoprIdee soprIdee) {
        soprIdee.setIdPorteur(1L);
        soprIdeeRepository.save(soprIdee);
    }

}
