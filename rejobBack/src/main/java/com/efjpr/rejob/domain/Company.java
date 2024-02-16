package com.efjpr.rejob.domain;

import com.efjpr.rejob.domain.Enums.CompanyType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "companies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnpj;
    private String name;
    private String businessActivity;
    private int numberOfEmployees;
    private Location headquarters;
    private String phone;
    private String institutionalDescription;
    private String email;
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Collaborator> collaborators;

    public void removeCollaborators() {
        this.collaborators = null;
    }
}
