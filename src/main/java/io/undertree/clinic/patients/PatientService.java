package io.undertree.clinic.patients;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * PatientService Interface
 *
 * Applying the security "rules" in the interface is a bit of a stylistic
 * decision to keep the implementation code free of the annotation noise.
 *
 * Using SPEL expressions can get complex which is why I generally end up
 * delegating the logic through to a @Component (or just do a custom
 * annotation).  It also makes unit testing a bit easier...
 */
//@Secured({"ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_PATIENT"})
public interface PatientService {

    @PostAuthorize("@patientAuthz.isAllowed(authentication, #id, returnObject)")
    Optional<Patient> getPatient(Long id);

    @PreAuthorize("@patientAuthz.isAllowed(authentication, #id, #patient)")
    Optional<Patient> updatePatient(Long id, Patient patient);
}
