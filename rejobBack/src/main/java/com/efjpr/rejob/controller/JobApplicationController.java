package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Dto.JobApplicationRequest;
import com.efjpr.rejob.domain.Dto.updateJobApplication;
import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.JobApplication;
import com.efjpr.rejob.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/jobApplications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @Autowired
    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    public ResponseEntity<JobApplication> createJobApplication(@RequestBody JobApplicationRequest jobApplication) {
        JobApplication createdJobApplication = jobApplicationService.saveJobApplication(jobApplication);
        return new ResponseEntity<>(createdJobApplication, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getJobApplicationById(@PathVariable Long id) {
        Optional<JobApplication> jobApplicationOptional = jobApplicationService.findJobApplicationById(id);
        return jobApplicationOptional.map(jobApplication -> new ResponseEntity<>(jobApplication, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{applicantId}/{jobId}")
    public ResponseEntity<JobApplication> getJobApplicationApplicantIdAndJobId(@PathVariable Long applicantId, @PathVariable Long jobId) {
        JobApplication jobApplication= jobApplicationService.findByApplicantAndJob(applicantId, jobId);
        return new ResponseEntity<>(jobApplication, HttpStatus.OK);
    }

    @GetMapping("my-applications/{employeeId}")
    public ResponseEntity<List<JobApplication>> getApplications(@PathVariable Long employeeId) {
        return new ResponseEntity<>(jobApplicationService.findByEmployeeId(employeeId), HttpStatus.OK);
    }

    @GetMapping("applicants/{JobId}")
    public ResponseEntity<List<Employee>> findApplicantsByJobId(@PathVariable Long JobId) {
        return new ResponseEntity<>(jobApplicationService.findApplicantsByJobId(JobId), HttpStatus.OK);
    }



    @GetMapping
    public ResponseEntity<List<JobApplication>> getAllJobApplications() {
        List<JobApplication> jobApplications = jobApplicationService.getAllJobApplications();
        return new ResponseEntity<>(jobApplications, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplicationById(@PathVariable Long id) {
        jobApplicationService.deleteJobApplicationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{applicantId}/{jobId}")
    public ResponseEntity<JobApplication> updateJobApplication(@PathVariable Long applicantId, @PathVariable Long jobId, @RequestBody updateJobApplication request) {
        JobApplication jobApplication = jobApplicationService.updateJobApplication(applicantId, jobId,request.getStatus(), request.getFeedback());
        return new ResponseEntity<>(jobApplication, HttpStatus.OK);
    }
}
