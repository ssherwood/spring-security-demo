package io.undertree.clinic.patients;

import io.undertree.clinic.common.BadRequestException;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String givenName;

    public boolean canUpdate(Patient patient) {
        return (Objects.equals(id, patient.id) && Objects.equals(username, patient.username));
    }

    public Patient applyDelta(Patient applyWith) {
        // apply only "modifyable" fields
        givenName = applyWith.givenName;
        return this;
    }
}
