package io.undertree.clinic.patients;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Optional<Patient> getPatient(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> updatePatient(Long id, Patient patient) {
        return patientRepository.findById(id)
                .map(p -> patientRepository.save(p.applyDelta(patient)));
    }
}
