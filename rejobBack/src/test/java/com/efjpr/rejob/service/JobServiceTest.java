package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.*;
import com.efjpr.rejob.domain.Dto.JobCreate;
import com.efjpr.rejob.domain.Dto.JobResponse;
import com.efjpr.rejob.domain.Enums.*;
import com.efjpr.rejob.repository.CollaboratorRepository;
import com.efjpr.rejob.repository.JobRepository;
import org.checkerframework.checker.units.qual.C;
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

    private Job job;

    private Company company;

    private Collaborator collaborator;

    private User user;

    @BeforeEach
    public void setUp() {
        job = Job.builder()
                .id(1L)
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

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .role(Role.USER)
                .email("johndoe@example.com")
                .password("password123")
                .phoneNumber("123-456-7890")
                .createdDate(new Date(2000, Calendar.JANUARY, 1))
                .lastUpdatedDate(new Date(2000, Calendar.JANUARY, 1))
                .profilePic("profile_pic_url")
                .build();

        company = Company.builder()
                .id(1L)
                .companyType(CompanyType.EMPRESA_COMERCIAL)
                .cnpj("12345678901234")
                .businessActivity("Lanchonete")
                .phone("123-456-7890")
                .email("passaportedoalemao@example.com")
                .institutionalDescription("Os melhores passaportes da cidade")
                .numberOfEmployees(10)
                .user(user)
                .headquarters(new Location("Maceio", "Alagoas", "Cruz das Almas"))
                .name("Churrasquinho do Calvo")
                .build();

        collaborator = Collaborator.builder()
                .id(1L)
                .user(user)
                .jobTitle("Cozinheiro")
                .collaboratorType(CollaboratorType.PRIVATE_ENTERPRISE)
                .company(company)
                .build();
    }

    @Test
    void testGetAllJobs() {
        Collaborator contactPerson1 = collaborator;
        contactPerson1.setCompany(company);
        Job job1 = job;
        job1.setContactPerson(contactPerson1);

        Collaborator contactPerson2 = new Collaborator(2L, user, "Contador", CollaboratorType.ONG, company);
        contactPerson2.setCompany(company);
        Job job2 = job;
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

        Collaborator contactPerson = collaborator;
        when(collaboratorRepository.findById(jobCreate.getContactPersonId())).thenReturn(Optional.of(contactPerson));

        Job job = jobService.createJob(jobCreate);

        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testGetJobById() {
        Collaborator contactPerson = collaborator;
        contactPerson.setCompany(company);
        Job job1 = job;
        job.setContactPerson(contactPerson);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job1));

        JobResponse jobResponse = jobService.getJobById(1L);

        assertEquals(job1.getId(), jobResponse.getId());
    }

    @Test
    void testGetJobByIdNotFound() {
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> jobService.getJobById(1L));
    }

    @Test
    void testUpdateJob() {
        Long id = 1L;
        Job updatedJob = job;
        updatedJob.setId(id);
        updatedJob.setJobTitle("Marceneiro");
        updatedJob.setJobDescription("Trabalhar com marcenaria");
        updatedJob.setEducationLevel(EducationLevel.ENSINO_FUNDAMENTAL_COMPLETO);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(updatedJob));
        when(jobRepository.save(updatedJob)).thenReturn(updatedJob);

        Job result = jobService.updateJob(1L, updatedJob);

        verify(jobRepository, times(1)).save(updatedJob);
        assertEquals(updatedJob.getJobTitle(), result.getJobTitle());
    }

    @Test
    void testUpdateJobNotFound() {
        Job updatedJob = job;

        when(jobRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> jobService.updateJob(2L, updatedJob));
    }

    @Test
    void testDeleteJob() {
        Job existingJob = job;

        when(jobRepository.findById(1L)).thenReturn(Optional.of(existingJob));

        jobService.deleteJob(1L);

        verify(jobRepository, times(1)).delete(existingJob);
    }

    @Test
    void testDeleteJobNotFound() {
        when(jobRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> jobService.deleteJob(2L));
    }

}
