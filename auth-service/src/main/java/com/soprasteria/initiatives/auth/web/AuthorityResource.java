package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Resource publishing {@link Authority} entity
 *
 * @author jntakpe
 */
@RestController
@RequestMapping(ApiConstants.AUTHORITIES)
public class AuthorityResource {

    private final AuthorityService authorityService;

    @Autowired
    public AuthorityResource(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Authority> findAll() {
        return authorityService.findAll();
    }

    @RequestMapping(value = "/{authorityId}", method = RequestMethod.GET)
    public Authority findById(@PathVariable Long authorityId) {
        return authorityService.findById(authorityId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Authority create(@RequestBody @Valid Authority authority) {
        return authorityService.create(authority);
    }

    @RequestMapping(value = "/{authorityId}", method = RequestMethod.PUT)
    public Authority edit(@PathVariable Long authorityId, @RequestBody @Valid Authority authority) {
        return authorityService.edit(authorityId, authority);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{authorityId}", method = RequestMethod.DELETE)
    public void remove(@PathVariable Long authorityId) {
        authorityService.delete(authorityId);
    }

}
