package com.soprasteria.initiatives.web;

import com.soprasteria.initiatives.domain.Authority;
import com.soprasteria.initiatives.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

/**
 * Resource publishing {@link Authority} entity
 *
 * @author jntakpe
 * @author cegiraud
 */
@RestController
@RequestMapping(ApiConstants.AUTHORITIES)
public class AuthorityResource {

    private final AuthorityService authorityService;

    public AuthorityResource(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping
    public Flux<Authority> findAll() {
        return authorityService.findAll();
    }

    @GetMapping("/{authorityId}")
    public Mono<Authority> findById(@PathVariable String authorityId) {
        return authorityService.findById(authorityId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Authority> create(@RequestBody @Valid Authority authority) {
        return authorityService.create(authority);
    }

    @PutMapping("/{authorityId}")
    public Mono<Authority> edit(@PathVariable String authorityId, @RequestBody @Valid Authority authority) {
        return authorityService.edit(authorityId, authority);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{authorityId}")
    public Mono<Void> remove(@PathVariable String authorityId) {
        return authorityService.delete(authorityId);
    }

}
