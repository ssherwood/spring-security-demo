package io.undertree.clinic.patient;

import io.undertree.clinic.patients.Patient;
import io.undertree.clinic.patients.PatientAuthz;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class PatientAuthzTests {

    private PatientAuthz patientAuthz = new PatientAuthz();
    private UsernamePasswordAuthenticationToken patient1Auth;
    private Patient patient1 = new Patient(1L, "foo", "Foo");
    private Patient patient2 = new Patient(2L, "bar", "Bar");

    @Before
    public void setUp() throws Exception {
        patient1Auth = new UsernamePasswordAuthenticationToken("foo", "bar", asList(new SimpleGrantedAuthority("ROLE_PATIENT")));
    }

    @Test
    public void givenNull_whenIsAllowedCalled_thenExpectFalse() throws Exception {
        assertThat(patientAuthz.isAllowed(null, null, null)).isFalse();
        assertThat(patientAuthz.isAllowed(null, 1L, null)).isFalse();
        assertThat(patientAuthz.isAllowed(null, 1L, Optional.empty())).isFalse();
        assertThat(patientAuthz.isAllowed(null, null, Optional.empty())).isFalse();
        assertThat(patientAuthz.isAllowed(patient1Auth, 1L, Optional.empty())).isFalse();
        assertThat(patientAuthz.isAllowed(patient1Auth, null, Optional.empty())).isFalse();
        assertThat(patientAuthz.isAllowed(patient1Auth, null, null)).isFalse();
    }

    @Test
    public void givenPatient1_whenIsAllowedCalledWithPatient2_thenExpectFalse() throws Exception {
        assertThat(patientAuthz.isAllowed(patient1Auth, 1L, Optional.of(patient2))).isFalse();
    }

    @Test
    public void givenPatient1_whenIsAllowedCalledWithPatient1_thenExpectTrue() throws Exception {
        assertThat(patientAuthz.isAllowed(patient1Auth, 1L, Optional.of(patient1))).isTrue();
    }
}
