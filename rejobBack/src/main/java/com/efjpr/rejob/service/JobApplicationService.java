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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final EmployeeService employeeService;
    private final JobService jobService;
    private final JobRecommendationService jobRecommendationService;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository, EmployeeService employeeService, JobService jobService, JobRecommendationService jobRecommendationService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.employeeService = employeeService;
        this.jobService = jobService;
        this.jobRecommendationService = jobRecommendationService;
    }


    public JobApplication saveJobApplication(JobApplicationRequest jobApplicationRequest) {
        Employee applicant = employeeService.findById(jobApplicationRequest.getApplicantId());
        Job job = jobService.findById(jobApplicationRequest.getJobId());
        jobService.updateHasNewApplicant(job, true);
        double score = jobRecommendationService.similarity(applicant, job);
        if (applicationExists(applicant, job)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already applied");
        }
        JobApplication jobApplication = JobApplication.builder()
                .applicant(applicant)
                .job(job).applicationDate(Date.from(Instant.now()))
                .status(jobApplicationRequest.getStatus())
                .feedback(jobApplicationRequest.getFeedback())
                .similarity(score)
                .build();

        return jobApplicationRepository.save(jobApplication);
    }

    public boolean applicationExists(Employee applicant, Job job) {
        return jobApplicationRepository.existsByApplicantAndJob(applicant, job);
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

    public JobApplication findByApplicantAndJob(Long employeeId, Long jobId) {
        Employee applicant = employeeService.findById(employeeId);
        Job job = jobService.findById(jobId);
        return jobApplicationRepository.findByApplicantAndJob(applicant, job);
    }


    public JobApplication updateJobApplication(Long employeeId, Long jobId, ApplicationStatus status, String feedback) {
        JobApplication jobApplication = findByApplicantAndJob(employeeId, jobId);
        jobApplication.setStatus(status);
        jobApplication.setFeedback(feedback);
        jobApplication.setSimilarity(jobApplication.getSimilarity());
        return jobApplicationRepository.save(jobApplication);
    }

    public List<JobApplication> findByEmployeeId(Long employeeId) {
        Employee applicant = employeeService.findById(employeeId);
        return jobApplicationRepository.findAllByApplicant(applicant);
    }

    public List<JobApplication> findByJobId(Long jobId) {
        if (jobId == null) {
            return Collections.emptyList();
        }
        List<JobApplication> jobApplications = jobApplicationRepository.findAllByJobId(jobId);

        return jobApplications.stream()
                .sorted(Comparator.comparing(JobApplication::getSimilarity).reversed())
                .collect(Collectors.toList());
    }

    public List<Employee> findApplicantsByJobId(Long jobId) {
        if (jobId == null) {
            return Collections.emptyList();
        }
        return jobApplicationRepository.findAllByJobId(jobId).stream().map(JobApplication::getApplicant).collect(Collectors.toList());
    }
}
