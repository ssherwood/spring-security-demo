package io.undertree.clinic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.discovery.ProviderConfiguration;
import org.springframework.security.oauth2.client.discovery.ProviderDiscoveryClient;
import org.springframework.security.oauth2.provider.token.store.IssuerClaimVerifier;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
import org.springframework.stereotype.Component;

// TODO - Working around error in startup due to duplicate jwkTokenStore
@Profile("cloudz")
@Slf4j
@Component
public class JwtPostProcessor implements BeanPostProcessor {
    @Value("${ssoServiceUrl}")
    private String ssoServiceUrl;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.resource.jwk.key-set-uri}")
    private String keySetUri;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.toLowerCase().contains("token")) {
            log.info("!!! Processing bean " + beanName + " -> " + bean.getClass().getCanonicalName());
        }

        if (bean instanceof JwkTokenStore) {
            JwkTokenStore jwkTokenStore = (JwkTokenStore) bean;
            log.warn("!!!Postprocessing " + jwkTokenStore);
            ProviderDiscoveryClient discoveryClient = new ProviderDiscoveryClient(this.ssoServiceUrl);
            ProviderConfiguration providerConfiguration = discoveryClient.discover();
            IssuerClaimVerifier issuerClaimVerifier = new IssuerClaimVerifier(providerConfiguration.getIssuer());
            return new JwkTokenStore(this.keySetUri, issuerClaimVerifier);
        }
        return bean;
    }
}
