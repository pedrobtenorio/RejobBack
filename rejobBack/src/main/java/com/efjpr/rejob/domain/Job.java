package com.efjpr.rejob.domain;

import com.efjpr.rejob.domain.Enums.EducationLevel;
import com.efjpr.rejob.domain.Enums.EmploymentContractType;
import com.efjpr.rejob.domain.Enums.JobStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "job")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Location companyLocation;
    private String jobType;
    private String categories;

    @ManyToOne(fetch = FetchType.EAGER)
    private Collaborator contactPerson;

    private String jobTitle;
    private String requirements;
    private String jobDescription;
    private String benefits;
    private String employmentType;
    private Date applicationDeadline;
    private SalaryRange salaryRange;
    private String responsibilities;
    private String requiredExperience;

    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;

    @Enumerated(EnumType.STRING)
    private EmploymentContractType employmentContractType;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}