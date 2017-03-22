package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.service.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Resource providing simplified access to token
 *
 * @author jntakpe
 * @author cegiraud
 */
@RestController
@RequestMapping("lol")
public class SecureResource {

    private final TokenService tokenService;

    public SecureResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("s")
    @ResponseBody
    String l() {
        return "loooool";
    }


    @GetMapping("all")
    @ResponseBody
    String la() {
        return "tttt";
    }
}