package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.JobCreate;
import com.efjpr.rejob.domain.Dto.JobResponse;
import com.efjpr.rejob.domain.Dto.StatusRequest;
import com.efjpr.rejob.domain.Enums.JobStatus;
import com.efjpr.rejob.domain.Job;
import com.efjpr.rejob.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(description = "Cria uma vaga de emprego")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga criada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Colaborador com esse id não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobCreate job) {
        return new ResponseEntity<>(jobService.createJob(job), HttpStatus.CREATED);
    }

    @Operation(description = "Retorna todas as vagas de emprego")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vagas retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
    }

    @Operation(description = "Retorna todas as vagas abertas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vagas abertas retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/open")
    public ResponseEntity<List<JobResponse>> getAllOpenJobs() {
        return new ResponseEntity<>(jobService.getAllOpenJobs(), HttpStatus.OK);
    }

    @Operation(description = "Retorna as vagas recomendadas para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vagas recomendadas retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("recommendation/{employeeId}")
    public ResponseEntity<List<JobResponse>> recommendedJobsForUser(@PathVariable  Long employeeId) {
        return new ResponseEntity<>(jobService.getRecommendedJobs(employeeId), HttpStatus.OK);
    }

    @Operation(description = "Retorna todas as vagas associadas a um colaborador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vagas associadas a colaborador retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("job-by-collaborator/{collaboratorId}")
    public ResponseEntity<List<JobResponse>> getAllJobsbyCollaboratorId(@PathVariable Long collaboratorId) {
        return new ResponseEntity<>(jobService.getAllJobsByCollaboratorId(collaboratorId), HttpStatus.OK);
    }

    @Operation(description = "Retorna todas as vagas de uma empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vagas da empresa retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("job-list/{companyId}")
    public ResponseEntity<List<Job>> getJobByCompanyId(@PathVariable Long companyId) {
        return new ResponseEntity<>(jobService.getJobByCompanyId(companyId), HttpStatus.OK);
    }

    @Operation(description = "Retorna vaga pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma vaga com esse id foi encontrada"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return new ResponseEntity<>(jobService.getJobById(id), HttpStatus.OK);
    }

    @Operation(description = "Altera informações de uma vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações atualizadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma vaga com esse id foi encontrada / Colaborador com esse id não encontrado"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody JobCreate updatedJob) {
        return new ResponseEntity<>(jobService.updateJob(id, updatedJob), HttpStatus.OK);
    }

    @Operation(description = "Atualiza o status da vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da vaga atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma vaga com esse id foi encontrada"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<JobResponse> changeStatus(@PathVariable Long id, @RequestBody StatusRequest newStatus) {
        return new ResponseEntity<>(jobService.updateJobStatus(id, newStatus), HttpStatus.OK);
    }

    @Operation(description = "Busca e retorna vagas com os parâmetros fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/jobs")
    public List<JobResponse> searchJobs(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String categories,
                                @RequestParam(required = false) Float minSalary,
                                @RequestParam(required = false) Float maxSalary,
                                @RequestParam(required = false) String state) {
        return jobService.searchJobs(name, categories, minSalary, maxSalary, state);
    }

    @Operation(description = "Deleta uma vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma vaga com esse id foi encontrada"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
