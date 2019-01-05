package io.undertree.clinic.patients;

import io.undertree.clinic.common.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class PatientController {
    private final PatientService patientService;


    @Value("${vcap.services.credhub-cred.credentials.password:UNKNOWN}")
    private String credhubSecret;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/credhub")
    public String getCredhubSecret() {
        return credhubSecret;
    }

    @GetMapping("/patients/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientService.getPatient(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/patients/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient).orElseThrow(NotFoundException::new);
    }
}
