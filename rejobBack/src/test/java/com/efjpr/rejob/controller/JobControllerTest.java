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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobController)
                .alwaysDo(print())
                .build();

        User user = User.builder()
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


        Company company = Company.builder()
                .id(1L)
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


        job = Job.builder()
                .id(1L)
                .companyLocation(new Location("Maceio", "Alagoas", "Jaragua"))
                .jobType("Programador")
                .categories("Júnior")
                .contactPerson(new Collaborator())
                .jobTitle("Desenvolvedor Back-End")
                .requirements("Conhecimentos básicos em Java e Springboot")
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
        Job job1 = job;

        when(jobService.createJob(jobCreate)).thenReturn(job1);

        ResponseEntity<Job> response = jobController.createJob(jobCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(job1, response.getBody());
    }

    @Test
    void testGetAllJobs() {
        List<JobResponse> jobs = Arrays.asList(new JobResponse(), new JobResponse());

        when(jobService.getAllJobs()).thenReturn(jobs);

        ResponseEntity<List<JobResponse>> response = jobController.getAllJobs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobs, response.getBody());
    }

    @Test
    void testGetJobByCompanyId() {
        Long companyId = 1L;
        List<Job> jobs = Arrays.asList(job, new Job());

        when(jobService.getJobByCompanyId(companyId)).thenReturn(jobs);

        ResponseEntity<List<Job>> response = jobController.getJobByCompanyId(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobs, response.getBody());
    }

    @Test
    void testGetJobById() {
        Long id = 1L;
        JobResponse jobResponse = new JobResponse();

        when(jobService.getJobById(id)).thenReturn(jobResponse);

        ResponseEntity<JobResponse> response = jobController.getJobById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobResponse, response.getBody());
    }

    @Test
    void testUpdateJob() {
        Long id = 1L;
        Job updatedJob = job;
        updatedJob.setRequiredExperience("Nenhuma");
        updatedJob.setCompanyLocation(new Location("Recife", "Pernambuco", "Boa Viagem"));

        when(jobService.updateJob(id, updatedJob)).thenReturn(updatedJob);

        ResponseEntity<Job> response = jobController.updateJob(id, updatedJob);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedJob, response.getBody());
    }

    @Test
    void testDeleteJob() {
        Long id = 1L;
        Job job1 = job;

        ResponseEntity<Void> response = jobController.deleteJob(id);

        verify(jobService).deleteJob(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
