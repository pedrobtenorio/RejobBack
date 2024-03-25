package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Dto.JobApplicationRequest;
import com.efjpr.rejob.domain.Dto.updateJobApplication;
import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.JobApplication;
import com.efjpr.rejob.service.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(description = "Cria aplicação para vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aplicação para vaga criada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Já aplicou para esta vaga"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PostMapping
    public ResponseEntity<JobApplication> createJobApplication(@RequestBody JobApplicationRequest jobApplication) {
        JobApplication createdJobApplication = jobApplicationService.saveJobApplication(jobApplication);
        return new ResponseEntity<>(createdJobApplication, HttpStatus.CREATED);
    }

    @Operation(description = "Retorna aplicação para uma vaga por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aplicação para vaga retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma aplicação para vaga com esse id foi encontrada"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getJobApplicationById(@PathVariable Long id) {
        Optional<JobApplication> jobApplicationOptional = jobApplicationService.findJobApplicationById(id);
        return jobApplicationOptional.map(jobApplication -> new ResponseEntity<>(jobApplication, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(description = "Retorna aplicante e respectiva vaga pelos seus id's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aplicação e vaga retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/{applicantId}/{jobId}")
    public ResponseEntity<JobApplication> getJobApplicationApplicantIdAndJobId(@PathVariable Long applicantId, @PathVariable Long jobId) {
        JobApplication jobApplication= jobApplicationService.findByApplicantAndJob(applicantId, jobId);
        return new ResponseEntity<>(jobApplication, HttpStatus.OK);
    }

    @Operation(description = "Retorna todas aplicações de vagas de um egresso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aplicações de vagas retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("my-applications/{employeeId}")
    public ResponseEntity<List<JobApplication>> getApplications(@PathVariable Long employeeId) {
        return new ResponseEntity<>(jobApplicationService.findByEmployeeId(employeeId), HttpStatus.OK);
    }

    @Operation(description = "Retorna todos aplicantes de uma vaga pelo id da vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aplicantes da vaga retornados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("applicants/{JobId}")
    public ResponseEntity<List<Employee>> findApplicantsByJobId(@PathVariable Long JobId) {
        return new ResponseEntity<>(jobApplicationService.findApplicantsByJobId(JobId), HttpStatus.OK);
    }

    @Operation(description = "Retorna uma vaga pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("job/{JobId}")
    public ResponseEntity<List<JobApplication>> findByJobId(@PathVariable Long JobId) {
        return new ResponseEntity<>(jobApplicationService.findByJobId(JobId), HttpStatus.OK);
    }


    @Operation(description = "Retorna todas as aplicações de vagas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aplicações de vagas retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping
    public ResponseEntity<List<JobApplication>> getAllJobApplications() {
        List<JobApplication> jobApplications = jobApplicationService.getAllJobApplications();
        return new ResponseEntity<>(jobApplications, HttpStatus.OK);
    }

    @Operation(description = "Deleta uma aplicação de vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aplicação de vaga deletada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplicationById(@PathVariable Long id) {
        jobApplicationService.deleteJobApplicationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Atualiza informações da aplicação de vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações atualizadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PutMapping("/{applicantId}/{jobId}")
    public ResponseEntity<JobApplication> updateJobApplication(@PathVariable Long applicantId, @PathVariable Long jobId, @RequestBody updateJobApplication request) {
        JobApplication jobApplication = jobApplicationService.updateJobApplication(applicantId, jobId,request.getStatus(), request.getFeedback());
        return new ResponseEntity<>(jobApplication, HttpStatus.OK);
    }
}
