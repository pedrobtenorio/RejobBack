package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /*@PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company createdCompany = companyService.create(company);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }*/

    @Operation(description = "Retorna empresa pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma empresa com esse id foi encontrada"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long companyId) {
        Company company = companyService.getCompanyById(companyId);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @Operation(description = "Retorna empresa pelo id de um colaborador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Colaborador não existe / Colaborador não está associado a nenhuma empresa"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("by-collaborator-id/{collaboratorId}")
    public ResponseEntity<Company> getCompanyByCollaboratorId(@PathVariable Long collaboratorId) {
        Company company = companyService.getCompanyByCollaboratorId(collaboratorId);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @Operation(description = "Retorna todos os colaboradores de uma mesma empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaboradores retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum colaborador está associado a essa empresa"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("collaborator-list/{collaboratorId}")
    public ResponseEntity<List<Collaborator>> getCollaboratorListFromSameCompany(@PathVariable Long collaboratorId) {
        Company company = companyService.getCompanyByCollaboratorId(collaboratorId);
        return new ResponseEntity<>(company.getCollaborators(), HttpStatus.OK);
    }

    @Operation(description = "Altera informações de uma empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações atualizadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma empresa com esse id foi encontrada"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PutMapping("/{companyId}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long companyId, @RequestBody Company company) {
        Company updatedCompany = companyService.updateCompany(companyId, company);
        return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
    }

    @Operation(description = "Deleta uma empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empresa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma empresa com esse id foi encontrada"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Retorna todas as empresas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresas retornadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        companyService.removeCollaborator(companies);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }
}
