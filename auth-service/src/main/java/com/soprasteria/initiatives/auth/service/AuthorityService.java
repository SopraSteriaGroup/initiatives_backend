package com.soprasteria.initiatives.auth.service;

import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

/**
 * Business services for {@link Authority} entity
 *
 * @author jntakpe
 */
@Service
public class AuthorityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityService.class);

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Transactional(readOnly = true)
    public List<Authority> findAll() {
        LOGGER.debug("Searching all authorities");
        return authorityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Authority findById(Long id) {
        LOGGER.debug("Searching authority for id : '{}'", id);
        Authority authority = authorityRepository.findOne(id);
        if (authority == null) {
            throw new EntityNotFoundException(String.format("Unable to retrieve id '%s'", id));
        }
        return authority;
    }

    @Transactional
    public Authority create(Authority authority) {
        LOGGER.info("Creating authority : {}", authority);
        checkNameAvailable(authority);
        return authorityRepository.save(authority);
    }

    @Transactional
    public Authority edit(Long id, Authority authority) {
        LOGGER.info("Updating authority : {}", authority);
        authority.setId(id);
        checkNameAvailable(authority);
        return authorityRepository.save(authority);
    }

    @Transactional
    public void delete(Long id) {
        Authority authority = findById(id);
        LOGGER.info("Deleting authority :  {}", authority);
        authority.removeUserLinks();
        authorityRepository.delete(authority);
    }

    @Transactional
    Authority findDefaultOrCreate() {
        return findByName(Authority.DEFAULT_AUTHORITY).orElseGet(() -> create(new Authority(Authority.DEFAULT_AUTHORITY)));
    }

    private Optional<Authority> findByName(String name) {
        LOGGER.debug("Searching authority with name '{}'", name);
        return authorityRepository.findByNameIgnoreCase(name);
    }

    private void checkNameAvailable(Authority authority) {
        Optional<Authority> opt = authorityRepository.findByNameIgnoreCase(authority.getName());
        if (opt.isPresent() && !opt.get().getId().equals(authority.getId())) {
            throw new ValidationException(String.format("Authority with name'%s' already used", authority.getName()));
        }
    }
}
