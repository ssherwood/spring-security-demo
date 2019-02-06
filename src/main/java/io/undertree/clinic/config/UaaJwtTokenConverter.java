package io.undertree.clinic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class UaaJwtTokenConverter extends DefaultAccessTokenConverter {

    // THIS DOESN'T SEEM TO DO ANYTHING

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
        OAuth2Authentication authentication = super.extractAuthentication(claims);
        authentication.setDetails(claims);
        log.info("Extracted auth {}", claims);
        return authentication;
    }
}