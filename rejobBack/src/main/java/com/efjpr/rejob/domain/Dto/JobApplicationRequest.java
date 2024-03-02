package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.Enums.ApplicationStatus;
import com.efjpr.rejob.domain.Job;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
public class JobApplicationRequest {


    private Long applicantId;

    private Long jobId;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String feedback;
}
