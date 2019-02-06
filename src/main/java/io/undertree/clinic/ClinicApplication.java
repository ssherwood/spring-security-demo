package io.undertree.clinic;

import io.undertree.clinic.config.SSLValidationDisabler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.net.ssl.*;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

// required to exclude issuercheck due to https://github.com/pivotal-cf/spring-cloud-sso-connector/issues/9
//
@SpringBootApplication //(exclude= {io.pivotal.spring.cloud.IssuerCheckConfiguration.class})
@EnableJpaAuditing
public class ClinicApplication {

    public static void main(String[] args) {
        if ("true".equals(System.getenv("SKIP_SSL_VALIDATION"))) {
            SSLValidationDisabler.disableSSLValidation();
        }

        SpringApplication.run(ClinicApplication.class, args);
    }

}