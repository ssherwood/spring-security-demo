package io.undertree.clinic.patient;

import io.undertree.clinic.patients.PatientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void givenUnauthenticated_whenServiceCalled_thenExpectException() throws Exception {
        patientService.getPatient(1L);
    }

    @Test
    @WithMockUser(username = "patient1", roles = {"PATIENT"})
    public void givenAuthenticated_whenServiceCalledForSelf_thenOk() throws Exception {
        patientService.getPatient(1L);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "patient1", roles = {"PATIENT"})
    public void givenAuthenticated_whenServiceCalledForSomeoneElse_thenExpectException() throws Exception {
        patientService.getPatient(2L);
    }

    @Test
    @WithMockUser(username = "doctor99", roles = {"DOCTOR"})
    public void givenAuthenticated_whenServiceCalledByDoctor_thenOk() throws Exception {
        patientService.getPatient(3L);
    }

    @Test
    @WithMockUser(username = "admin99", roles = {"ADMIN"})
    public void givenAuthenticated_whenServiceCalledByAdmin_thenOk() throws Exception {
        patientService.getPatient(3L);
    }
}