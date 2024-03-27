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
import org.mockito.Mock;
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

    @InjectMocks
    JobService jobService;

    @Mock
    JobRepository jobRepository;

    @Mock
    CollaboratorRepository collaboratorRepository;

    private Job job;

    private Company company;

    private Collaborator collaborator;

    private User user;

    private Long id = 1L;

    @BeforeEach
    public void setUp() {
        job = Job.builder()
                .id(id)
                .companyLocation(new Location("Maceio", "Alagoas", "Jaragua"))
                .categories("Category1")
                .contactPerson(new Collaborator())
                .jobTitle("Job Title")
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
                .id(id)
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
                .id(id)
                .companyType(CompanyType.PRIVATE_ENTERPRISE)
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
                .id(id)
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
        jobCreate.setId(id);
        jobCreate.setCompanyLocation(new Location("Maceio", "Alagoas", "Jaragua"));
        jobCreate.setContactPersonId(id);
        jobCreate.setJobTitle("Engenheiro de Software");

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

        when(jobRepository.findById(id)).thenReturn(Optional.of(job1));

        JobResponse jobResponse = jobService.getJobById(id);

        assertEquals(job1.getId(), jobResponse.getId());
    }

    @Test
    void testGetJobByIdNotFound() {
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> jobService.getJobById(id));
    }

    @Test
    void testUpdateJob() {
        Long jobId = 1L;
        Long contactPersonId = 2L;

        JobCreate updatedJob = new JobCreate();
        updatedJob.setContactPersonId(contactPersonId);
        updatedJob.setJobTitle("Software Engineer");
        updatedJob.setEmploymentType("CLT");
        updatedJob.setJobStatus(JobStatus.ACTIVE);

        Job existingJob = new Job();
        existingJob.setId(jobId);
        existingJob.setJobTitle("Software Developer");
        existingJob.setEmploymentType("PJ");
        existingJob.setJobStatus(JobStatus.IN_PROGRESS);

        Collaborator contactPerson = new Collaborator();
        contactPerson.setId(contactPersonId);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(existingJob));
        when(collaboratorRepository.findById(contactPersonId)).thenReturn(Optional.of(contactPerson));

        Job updatedJobResult = jobService.updateJob(jobId, updatedJob);

        verify(jobRepository, times(1)).findById(jobId);
        verify(collaboratorRepository, times(1)).findById(contactPersonId);
        verify(jobRepository, times(1)).save(existingJob);
    }

    @Test
    void testDeleteJob() {

        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        jobService.deleteJob(id);

        verify(jobRepository, times(1)).delete(job);
    }

    @Test
    void testDeleteJobNotFound() {
        when(jobRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> jobService.deleteJob(2L));
    }

}
