package io.undertree.clinic.patients;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class PatientAuthz {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *
     * FYI - it is generally considered a bad practice to accept an Optional as
     * input, however this case works better for the Service API since this
     * method will be used by a SPEL expression wrapping an Optional return
     * type.
     *
     * @param authentication
     * @param patientToView
     * @return
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public boolean isAllowed(Authentication authentication, Long patientId, Optional<Patient> patientToView) {
        if (authentication == null || StringUtils.isBlank(authentication.getName())) {
            logger.warn("Authz FAILED: invalid User '{}'.", authentication);
            return false;
        } else if (patientToView == null || !patientToView.isPresent()) {
            logger.warn("Authz FAILED: User '{}' requested Patient '{}' is missing.", authentication, patientId);
            return false;
        } else if (!Objects.equals(patientId, patientToView.get().getId())) {
            logger.warn("Authz FAILED: User '{}' requested Patient '{}' does not match requested Id '{}'.", authentication, patientToView.get().getId(), patientId);
            return false;
        } else if (isAdmin(authentication) || isDoctor(authentication)) {
            logger.info("Authz SUCCEEDED: allowing super user '{}' to access Patient '{}'", authentication, patientId);
            return true;
        }
        else if (Objects.equals(authentication.getName(), patientToView.get().getUsername())) {
            // ^^^ ideally we would use a unchangeable guid on the identity to test
            logger.info("Authz SUCCEEDED: allowing User '{}' access to Patient '{}'.", authentication, patientId);
            return true;
        }
        else {
            logger.warn("Authz FAILED: User '{}' not allowed to access Patient '{}'.", authentication, patientId);
            return false;
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }


    // this should be more sophisticated... are you a doctor that sees this
    // patient or are in the same practice?  etc?
    private boolean isDoctor(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_DOCTOR".equals(a.getAuthority()));
    }

    // other use cases?

    // is Person a Doctor for this patient
    // is Person a registered care giver for the patient?
}
