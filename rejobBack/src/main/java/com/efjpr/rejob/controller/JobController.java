package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.JobCreate;
import com.efjpr.rejob.domain.Dto.JobResponse;
import com.efjpr.rejob.domain.Dto.StatusRequest;
import com.efjpr.rejob.domain.Enums.JobStatus;
import com.efjpr.rejob.domain.Job;
import com.efjpr.rejob.service.JobService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobCreate job) {
        return new ResponseEntity<>(jobService.createJob(job), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
    }

    @GetMapping("/open")
    public ResponseEntity<List<JobResponse>> getAllOpenJobs() {
        return new ResponseEntity<>(jobService.getAllOpenJobs(), HttpStatus.OK);
    }

    @GetMapping("recommendation/{employeeId}")
    public ResponseEntity<List<JobResponse>> recommendedJobsForUser(@PathVariable  Long employeeId) {
        return new ResponseEntity<>(jobService.getRecommendedJobs(employeeId), HttpStatus.OK);
    }

    @GetMapping("job-by-collaborator/{collaboratorId}")
    public ResponseEntity<List<JobResponse>> getAllJobsbyCollaboratorId(@PathVariable Long collaboratorId) {
        return new ResponseEntity<>(jobService.getAllJobsByCollaboratorId(collaboratorId), HttpStatus.OK);
    }

    @GetMapping("job-list/{companyId}")
    public ResponseEntity<List<Job>> getJobByCompanyId(@PathVariable Long companyId) {
        return new ResponseEntity<>(jobService.getJobByCompanyId(companyId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return new ResponseEntity<>(jobService.getJobById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody JobCreate updatedJob) {
        return new ResponseEntity<>(jobService.updateJob(id, updatedJob), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<JobResponse> changeStatus(@PathVariable Long id, @RequestBody StatusRequest newStatus) {
        return new ResponseEntity<>(jobService.updateJobStatus(id, newStatus), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
