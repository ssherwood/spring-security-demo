package io.undertree.clinic.patients;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

public interface PatientService {

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_PATIENT"})
    @PostAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR') || @patientAuthz.isSelf(authentication, #id, returnObject)")
    Optional<Patient> getPatient(Long id);

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_PATIENT"})
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR') || @patientAuthz.isSelf(authentication, #id, #patient)")
    Optional<Patient> updatePatient(Long id, Patient patient);
}
