package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.Dto.JobApplicationRequest;
import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.Enums.ApplicationStatus;
import com.efjpr.rejob.domain.Job;
import com.efjpr.rejob.domain.JobApplication;
import com.efjpr.rejob.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final EmployeeService employeeService;
    private final JobService jobService;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository, EmployeeService employeeService, JobService jobService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.employeeService = employeeService;
        this.jobService = jobService;
    }


    public JobApplication saveJobApplication(JobApplicationRequest jobApplicationRequest) {
        Employee applicant = employeeService.findById(jobApplicationRequest.getApplicantId());
        Job job = jobService.findById(jobApplicationRequest.getJobId());

        JobApplication jobApplication = JobApplication.builder().applicant(applicant).job(job).applicationDate(Date.from(Instant.now())).status(jobApplicationRequest.getStatus()).feedback(jobApplicationRequest.getFeedback()).build();

        return jobApplicationRepository.save(jobApplication);
    }


    public Optional<JobApplication> findJobApplicationById(Long id) {
        return jobApplicationRepository.findById(id);
    }


    public List<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }


    public void deleteJobApplicationById(Long id) {
        jobApplicationRepository.deleteById(id);
    }


    public JobApplication updateJobApplication(Long id, ApplicationStatus status, String feedback) {
        JobApplication jobApplication = findJobApplicationById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application with id " + id + " not found"));
        jobApplication.setStatus(status);
        jobApplication.setFeedback(feedback);
        return jobApplicationRepository.save(jobApplication);
    }

    public List<JobApplication> findByEmployeeId(Long employeeId) {
        Employee applicant = employeeService.findById(employeeId);
        return jobApplicationRepository.findAllByApplicant(applicant);
    }

    public List<Employee> findApplicantsByJobId(Long jobId) {
        if (jobId == null) {
            return Collections.emptyList();
        }
        return jobApplicationRepository.findAllByJobId(jobId).stream()
                .map(JobApplication::getApplicant)
                .collect(Collectors.toList());
    }
}
