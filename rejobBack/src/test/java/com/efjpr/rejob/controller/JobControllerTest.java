package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.*;
import com.efjpr.rejob.domain.Dto.JobCreate;
import com.efjpr.rejob.domain.Dto.JobResponse;
import com.efjpr.rejob.domain.Enums.*;
import com.efjpr.rejob.domain.Job;
import com.efjpr.rejob.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class JobControllerTest {

    @InjectMocks
    JobController jobController;

    @Mock
    private JobService jobService;

    MockMvc mockMvc;

    private Job job;

    private Long id = 1L;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobController)
                .alwaysDo(print())
                .build();

        User user = User.builder()
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


        Company company = Company.builder()
                .id(id)
                .companyType(CompanyType.ONG)
                .cnpj("12345678901234")
                .businessActivity("Agricultura")
                .phone("123-456-7890")
                .email("company@example.com")
                .institutionalDescription("Description")
                .numberOfEmployees(100)
                .user(user)
                .headquarters(new Location("Maceio", "Alagoas", "Vergel"))
                .name("Company Name")
                .build();

        Collaborator collaborator = Collaborator.builder()
                .id(id)
                .user(user)
                .jobTitle("Cozinheiro")
                .collaboratorType(CollaboratorType.PRIVATE_ENTERPRISE)
                .company(company)
                .build();

        job = Job.builder()
                .id(id)
                .companyLocation(new Location("Maceio", "Alagoas", "Jaragua"))
                .categories("Júnior")
                .contactPerson(collaborator)
                .jobTitle("Desenvolvedor Back-End")
                .jobDescription("Trabalhar em equipe no desenvolvimentos de sistemas")
                .benefits("Dinheiro")
                .employmentType("Remoto")
                .applicationDeadline(new Date(2024, Calendar.DECEMBER, 30))
                .salaryRange(new SalaryRange(2000, 2500))
                .educationLevel(EducationLevel.ENSINO_MEDIO_COMPLETO)
                .employmentContractType(EmploymentContractType.CLT)
                .jobStatus(JobStatus.ACTIVE)
                .requiredExperience("Experiência em criação de aplicações utilizando Springboot")
                .responsibilities("Responsibilities")
                .createdAt(new Date(System.currentTimeMillis()))
                .updatedAt(new Date(System.currentTimeMillis()))
                .build();

    }

    @Test
    void testCreateJob() {
        JobCreate jobCreate = new JobCreate();
        when(jobService.createJob(jobCreate)).thenReturn(job);

        ResponseEntity<Job> responseEntity = jobController.createJob(jobCreate);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(job, responseEntity.getBody());
        verify(jobService, times(1)).createJob(jobCreate);
    }

    @Test
    void testGetAllJobs() {
        List<JobResponse> jobResponses = new ArrayList<>();
        jobResponses.add(createJobResponse(1L, "Cabelereiro"));
        jobResponses.add(createJobResponse(2L, "Motorista"));
        when(jobService.getAllJobs()).thenReturn(jobResponses);

        ResponseEntity<List<JobResponse>> responseEntity = jobController.getAllJobs();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(jobResponses, responseEntity.getBody());
        verify(jobService, times(1)).getAllJobs();
    }

    @Test
    void testGetJobByCompanyId() {
        List<Job> jobs = Arrays.asList(job, new Job());

        when(jobService.getJobByCompanyId(id)).thenReturn(jobs);

        ResponseEntity<List<Job>> response = jobController.getJobByCompanyId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobs, response.getBody());
    }

    @Test
    void testGetJobById() {
        JobResponse jobResponse = createJobResponse(2L, "Pintor");

        when(jobService.getJobById(id)).thenReturn(jobResponse);

        ResponseEntity<JobResponse> response = jobController.getJobById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobResponse, response.getBody());
    }

    @Test
    void testUpdateJob() {
        JobCreate existingJob = new JobCreate();
        existingJob.setJobTitle("Tesoureiro");
        existingJob.setCompanyLocation(new Location("Recife", "Pernambuco", "Boa Viagem"));

        // Mockando o retorno do jobService.updateJob para retornar um objeto Job
        Job updatedJob = new Job();
        updatedJob.setId(1L);
        updatedJob.setJobTitle(existingJob.getJobTitle());
        updatedJob.setCompanyLocation(existingJob.getCompanyLocation());

        when(jobService.updateJob(id, existingJob)).thenReturn(updatedJob);

        // Chamando o método do controller que utiliza o serviço de job
        ResponseEntity<Job> response = jobController.updateJob(id, existingJob);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteJob() {

        ResponseEntity<Void> response = jobController.deleteJob(id);

        verify(jobService).deleteJob(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private JobResponse createJobResponse(Long id, String title) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setId(id);
        jobResponse.setJobTitle(title);
        return jobResponse;
    }

}
