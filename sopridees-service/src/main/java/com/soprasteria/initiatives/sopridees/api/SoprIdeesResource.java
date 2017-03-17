package com.soprasteria.initiatives.sopridees.api;

import com.soprasteria.initiatives.sopridees.domain.SoprIdee;
import com.soprasteria.initiatives.sopridees.service.SoprIdeesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Ressource REST gérant les SoprIdees
 *
 * @author rjansem
 */
@Validated
@RestController
@RequestMapping(ApiConstants.SOPRIDEES_URI)
public class SoprIdeesResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoprIdeesResource.class);

    private final SoprIdeesService soprIdeesService;

    @Autowired
    public SoprIdeesResource(SoprIdeesService soprIdeesService) {
        this.soprIdeesService = soprIdeesService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<SoprIdee> findAll(Pageable page) {
        return soprIdeesService.findAll(page);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create(SoprIdee soprIdee) {
        soprIdeesService.create(soprIdee);
    }

}
