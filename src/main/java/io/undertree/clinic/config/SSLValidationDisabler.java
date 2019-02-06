package io.undertree.clinic.config;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 *
 */
@Slf4j
public class SSLValidationDisabler {

    /**
     * Install the all-trusting trust manager
     */
    public static void disableSSLValidation() {
        try {
            log.warn("WARNING!!!! Disabling SSL Validation!!! DO ***NOT*** USE THIS IN PRODUCTION!!!");

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (GeneralSecurityException e) {
            log.error("Issue with disabling the SSL trust store:", e);
        }
    }
}
