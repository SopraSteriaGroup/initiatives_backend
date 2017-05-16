package com.soprasteria.initiatives.auth.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.soprasteria.initiatives.auth.config.ProfileConstants;
import com.soprasteria.initiatives.auth.config.SecurityConstants;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

/**
 * Resource fake SSO endpoint
 *
 * @author cegiraud
 */
@RestController
@Profile(ProfileConstants.BOUCHON)
public class FakeSSOResource {

    @GetMapping(ApiConstants.FAKE_SSO)
    @ResponseBody
    public String me(@RequestHeader String authorization) {
        String token = StringUtils.substringAfter(authorization, SecurityConstants.BEARER_PREFIX);
        return new String(Base64.getUrlDecoder().decode(token));
    }

    /**
     * Endpoint facilitant la génération de token.
     * Utiliser ce token pour s'authentifier en POST sur le même endpoint avec :
     * - accessToken = <token réccupéré>
     * - ssoProdider = fakesso
     *
     * @param idsso     : l'idsso souhaité.
     * @param firstName : le prénom de l'utilisateur
     * @param lastName  : le nom de l'utiliateur
     * @return l'accessToken
     */
    @ApiOperation(value = "Endpoint facilitant la génération de token",
            notes = "Utiliser le token réccupéré pour s'authentifier en POST avec :\n" +
                    "* accessToken = &lt;le token réccupéré&gt;\n" +
                    "* ssoProdider = fakesso")
    @GetMapping(ApiConstants.AUTHENTICATION + ApiConstants.TOKENS)
    @ResponseBody
    public String createAccessToken(@RequestParam String idsso, @RequestParam String firstName, @RequestParam String lastName) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ImmutableMap user = ImmutableMap.builder()
                .put("id", idsso)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .build();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(objectMapper.writeValueAsBytes(user));
    }
}