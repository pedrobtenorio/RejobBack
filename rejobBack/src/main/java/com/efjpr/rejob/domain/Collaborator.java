package com.efjpr.rejob.domain;

import com.efjpr.rejob.domain.Enums.CollaboratorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Collaborators")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collaborator  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private String jobTitle;

    @Enumerated(EnumType.STRING)
    private CollaboratorType collaboratorType;

    @ManyToOne
    @JsonIgnore
    private Company company;

    public Long getCompanyId() {
        if (company != null) {
            return company.getId();
        }
        return null; // or throw an exception, depending on your requirements
    }

}
