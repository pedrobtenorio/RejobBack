package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.JobCreate;
import com.efjpr.rejob.domain.Dto.JobResponse;
import com.efjpr.rejob.domain.Job;
import com.efjpr.rejob.repository.CollaboratorRepository;
import com.efjpr.rejob.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final EmployeeService employeeService;
    private final JobRecommendationService jobRecommendationService;

    public List<JobResponse> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(this::convertToJobResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> getAllJobsByCollaboratorId(Long collaboratorId) {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .filter(job -> job.getContactPerson().getId().equals(collaboratorId))
                .map(this::convertToJobResponse)
                .collect(Collectors.toList());
    }

    public Job createJob(JobCreate jobPayload) {
        Collaborator contactPerson = collaboratorRepository.findById(jobPayload.getContactPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collaborator with ID " + jobPayload.getContactPersonId() + " not found"));

        Job job = buildJobFromPayload(jobPayload, contactPerson);

        return jobRepository.save(job);
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job with id " + id + " not found"));

        // Convert Job to JobResponse
        return convertToJobResponse(job);
    }

    public Job findById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job with id " + id + " not found"));
    }

    public List<Job> getJobByCompanyId(long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }

    public Job updateJob(Long id, JobCreate updatedJob) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job with id " + id + " not found") {
                });
        Collaborator contactPerson = collaboratorRepository.findById(updatedJob.getContactPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collaborator with ID " + updatedJob.getContactPersonId() + " not found"));

        validateAndApplyUpdates(existingJob, updatedJob, contactPerson);
        return jobRepository.save(existingJob);
    }

    public void deleteJob(Long id) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job with id " + id + " not found"));
        jobRepository.delete(existingJob);
    }

    private void validateAndApplyUpdates(Job existingJob, JobCreate updatedJob, Collaborator collaborator) {
        existingJob.setCompanyLocation(updatedJob.getCompanyLocation());
        existingJob.setCategories(updatedJob.getCategories());
        existingJob.setContactPerson(collaborator);
        existingJob.setJobTitle(updatedJob.getJobTitle());
        existingJob.setJobDescription(updatedJob.getJobDescription());
        existingJob.setBenefits(updatedJob.getBenefits());
        existingJob.setEmploymentType(updatedJob.getEmploymentType());
        existingJob.setApplicationDeadline(updatedJob.getApplicationDeadline());
        existingJob.setJobStatus(updatedJob.getJobStatus());

    }

    private Job buildJobFromPayload(JobCreate jobPayload, Collaborator contactPerson) {
        return Job.builder()
                .companyLocation(jobPayload.getCompanyLocation())
                .categories(jobPayload.getCategories())
                .contactPerson(contactPerson)
                .jobTitle(jobPayload.getJobTitle())
                .jobDescription(jobPayload.getJobDescription())
                .benefits(jobPayload.getBenefits())
                .employmentType(jobPayload.getEmploymentType())
                .applicationDeadline(jobPayload.getApplicationDeadline())
                .salaryRange(jobPayload.getSalaryRange())
                .educationLevel(jobPayload.getEducationLevel())
                .employmentContractType(jobPayload.getEmploymentContractType())
                .jobStatus(jobPayload.getJobStatus())
                .requiredExperience(jobPayload.getRequiredExperience())
                .responsibilities(jobPayload.getResponsibilities())
                .createdAt(new Date(System.currentTimeMillis()))
                .updatedAt(new Date(System.currentTimeMillis()))
                .build();
    }

    private JobResponse convertToJobResponse(Job job) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setId(job.getId());
        jobResponse.setCompanyLocation(job.getCompanyLocation());
        jobResponse.setCategories(job.getCategories());
        jobResponse.setContactPerson(job.getContactPerson());
        jobResponse.setJobTitle(job.getJobTitle());
        jobResponse.setJobDescription(job.getJobDescription());
        jobResponse.setBenefits(job.getBenefits());
        jobResponse.setEmploymentType(job.getEmploymentType());
        jobResponse.setApplicationDeadline(job.getApplicationDeadline());
        jobResponse.setSalaryRange(job.getSalaryRange());
        jobResponse.setResponsibilities(job.getResponsibilities());
        jobResponse.setRequiredExperience(job.getRequiredExperience());
        jobResponse.setEducationLevel(job.getEducationLevel());
        jobResponse.setEmploymentContractType(job.getEmploymentContractType());
        jobResponse.setJobStatus(job.getJobStatus());
        jobResponse.setCompanyName(job.getContactPerson().getCompany().getName());

        return jobResponse;
    }


    public List<JobResponse> getRecommendedJobs(Long employeeId) {
        List<Job> jobs = jobRecommendationService.getBestJobs(employeeService.findById(employeeId));
        return jobs.stream()
                .map(this::convertToJobResponse)
                .collect(Collectors.toList());
    }
}
