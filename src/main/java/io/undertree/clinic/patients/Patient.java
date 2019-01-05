package io.undertree.clinic.patients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String givenName;

    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastModifiedDate;

    public Patient(Long id, String username, String givenName) {
        this.id = id;
        this.username = username;
        this.givenName = givenName;
        //this.lastModifiedDate = new Date();@EntityListeners(AuditingEntityListener.class)
    }

    public Patient applyDelta(Patient applyWith) {
        // apply only "modifiable" fields
        return new Patient(id, username, applyWith.givenName);
    }
}
