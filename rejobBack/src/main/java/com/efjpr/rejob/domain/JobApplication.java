package com.efjpr.rejob.domain;

import com.efjpr.rejob.domain.Enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "job_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employee applicant;

    @ManyToOne
    private Job job;

    @Temporal(TemporalType.TIMESTAMP)
    private Date applicationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String feedback;


}
