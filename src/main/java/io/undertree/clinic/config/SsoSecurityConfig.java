package io.undertree.clinic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.REFERER;


@Profile("cloud")
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SsoSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ssoServiceUrl}")
    private String ssoServiceUrl;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.resource.jwk.key-set-uri}")
    private String keySetUri;

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails oa2prd, OAuth2ClientContext oa2cc) {
        return new OAuth2RestTemplate(oa2prd, oa2cc);
    }

//    @Bean
//    public AccessTokenProvider accessTokenProviderChain() {
//        return new AccessTokenProviderChain(Arrays.<AccessTokenProvider> asList(
//                new OpenIDTokenProvider(),
//                new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
//                new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider()));
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.csrf().disable();

        http
                .csrf().disable()
                // allows some browser interaction(s)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                //.exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                //.and()
                .authorizeRequests()
                .anyRequest().authenticated()
        //.antMatchers("/secured/**", "/api/**").authenticated()
        ;
        //.and()
        //.logout().logoutSuccessHandler(logoutSuccessHandler()).permitAll();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            String appLogoutSuccessUrl = request.getHeader(REFERER);
            String authServerLogoutUrl = ssoServiceUrl + "/logout.do?redirect=" + appLogoutSuccessUrl + "&client_id=" + clientId;
            response.setHeader("Location", authServerLogoutUrl);
            response.setStatus(HttpServletResponse.SC_FOUND);
        };
    }
}