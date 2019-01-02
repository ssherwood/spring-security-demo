package io.undertree.clinic.patients;

import io.undertree.clinic.common.BadRequestException;
import io.undertree.clinic.common.NotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
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
