package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.*;
import com.efjpr.rejob.domain.Dto.JobCreate;
import com.efjpr.rejob.domain.Dto.JobResponse;
import com.efjpr.rejob.domain.Enums.EducationLevel;
import com.efjpr.rejob.domain.Enums.EmploymentContractType;
import com.efjpr.rejob.domain.Enums.JobStatus;
import com.efjpr.rejob.domain.Enums.Role;
import com.efjpr.rejob.repository.CollaboratorRepository;
import com.efjpr.rejob.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    private final JobRepository jobRepository = mock(JobRepository.class);
    private final CollaboratorRepository collaboratorRepository = mock(CollaboratorRepository.class);
    private final JobService jobService = new JobService(jobRepository, collaboratorRepository);

    @BeforeEach
    public void setUp() {
        Job job = Job.builder()
                .companyLocation(new Location("Maceio", "Alagoas", "Jaragua"))
                .jobType("Job Type")
                .categories("Category1")
                .contactPerson(new Collaborator())
                .jobTitle("Job Title")
                .requirements("Requirements")
                .jobDescription("Job Description")
                .benefits("Benefits")
                .employmentType("Employment Type")
                .applicationDeadline(new Date(2024, Calendar.DECEMBER, 30))
                .salaryRange(new SalaryRange(2000, 2500))
                .educationLevel(EducationLevel.ENSINO_MEDIO_COMPLETO)
                .employmentContractType(EmploymentContractType.CLT)
                .jobStatus(JobStatus.ACTIVE)
                .requiredExperience("Required Experience")
                .responsibilities("Responsibilities")
                .createdAt(new Date(System.currentTimeMillis()))
                .updatedAt(new Date(System.currentTimeMillis()))
                .build();
    }

    @Test
    void testGetAllJobs() {
        Collaborator contactPerson1 = new Collaborator();
        contactPerson1.setCompany(new Company());
        Job job1 = new Job();
        job1.setContactPerson(contactPerson1);

        Collaborator contactPerson2 = new Collaborator();
        contactPerson2.setCompany(new Company());
        Job job2 = new Job();
        job2.setContactPerson(contactPerson2);

        List<Job> jobs = Arrays.asList(job1, job2);
        when(jobRepository.findAll()).thenReturn(jobs);

        List<JobResponse> jobResponses = jobService.getAllJobs();

        assertEquals(2, jobResponses.size());
    }


    @Test
    void testCreateJob() {
        JobCreate jobCreate = new JobCreate();
        jobCreate.setContactPersonId(1L);

        Collaborator contactPerson = new Collaborator();
        when(collaboratorRepository.findById(jobCreate.getContactPersonId())).thenReturn(Optional.of(contactPerson));

        Job job = jobService.createJob(jobCreate);

        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testGetJobById() {
        Collaborator contactPerson = new Collaborator();
        contactPerson.setCompany(new Company()); // Assuming Collaborator has a Company association
        Job job = new Job();
        job.setContactPerson(contactPerson);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        JobResponse jobResponse = jobService.getJobById(1L);

        assertEquals(job.getId(), jobResponse.getId());
    }

    @Test
    void testGetJobByIdNotFound() {
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> jobService.getJobById(1L));
    }

    @Test
    void testUpdateJob() {
        Job existingJob = new Job();
        existingJob.setId(1L);
        Job updatedJob = new Job();
        updatedJob.setId(1L);
        updatedJob.setJobTitle("Updated Title");

        when(jobRepository.findById(1L)).thenReturn(Optional.of(existingJob));
        when(jobRepository.save(existingJob)).thenReturn(updatedJob);

        Job result = jobService.updateJob(1L, updatedJob);

        verify(jobRepository, times(1)).save(existingJob);
        assertEquals(updatedJob.getJobTitle(), result.getJobTitle());
    }

    @Test
    void testUpdateJobNotFound() {
        Job updatedJob = new Job();
        updatedJob.setId(1L);

        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> jobService.updateJob(1L, updatedJob));
    }

    @Test
    void testDeleteJob() {
        Job job = new Job();
        job.setId(1L);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        jobService.deleteJob(1L);

        verify(jobRepository, times(1)).delete(job);
    }

    @Test
    void testDeleteJobNotFound() {
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> jobService.deleteJob(1L));
    }

}
