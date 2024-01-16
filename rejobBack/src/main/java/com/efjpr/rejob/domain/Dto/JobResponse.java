package com.efjpr.rejob.domain.Dto;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Enums.EducationLevel;
import com.efjpr.rejob.domain.Enums.EmploymentContractType;
import com.efjpr.rejob.domain.Enums.JobStatus;
import com.efjpr.rejob.domain.SalaryRange;
import lombok.Data;

import java.util.Date;

@Data
public class JobResponse {

    private Long id;
    private String companyLocation;
    private String jobType;
    private String categories;
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
    private EducationLevel educationLevel;
    private EmploymentContractType employmentContractType;
    private JobStatus jobStatus;
    private String companyName;
}
