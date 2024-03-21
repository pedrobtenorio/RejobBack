package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Enums.CollaboratorType;
import com.efjpr.rejob.domain.Enums.CompanyType;
import com.efjpr.rejob.domain.Enums.Role;
import com.efjpr.rejob.domain.Location;
import com.efjpr.rejob.domain.User;
import com.efjpr.rejob.service.CollaboratorService;
import com.efjpr.rejob.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    private CollaboratorService collaboratorService;

    MockMvc mockMvc;

    private Company company;

    private Collaborator collaborator;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController)
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
                .name("Passaporte do Alemão")
                .build();

        Collaborator collaborator = Collaborator.builder()
                .user(user)
                .jobTitle("Cozinheiro")
                .collaboratorType(CollaboratorType.PRIVATE_ENTERPRISE)
                .company(company)
                .build();

    }

    @Test
    void testGetCompanyById() {
        Long companyId = 1L;
        Company company1 = company;
        company1.setId(companyId);

        when(companyService.getCompanyById(companyId)).thenReturn(company1);

        ResponseEntity<Company> response = companyController.getCompanyById(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(company1, response.getBody());
    }

    @Test
    void testGetCompanyByCollaboratorId() {
        Long collaboratorId = 1L;
        Company company1 = company;

        when(companyService.getCompanyByCollaboratorId(collaboratorId)).thenReturn(company1);

        ResponseEntity<Company> response = companyController.getCompanyByCollaboratorId(collaboratorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(company1, response.getBody());
    }

    @Test
    void testGetCollaboratorListFromSameCompany() {
        Long collaboratorId = 1L;
        Company company1 = company;

        when(companyService.getCompanyByCollaboratorId(collaboratorId)).thenReturn(company1);

        ResponseEntity<List<Collaborator>> response = companyController.getCollaboratorListFromSameCompany(collaboratorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateCompany() {
        Long companyId = 1L;
        Company updatedCompany = company;
        updatedCompany.setName("Passaporte do Gaúcho");
        updatedCompany.setNumberOfEmployees(15);
        updatedCompany.setCompanyType(CompanyType.ONG);

        when(companyService.updateCompany(companyId, updatedCompany)).thenReturn(updatedCompany);

        ResponseEntity<Company> response = companyController.updateCompany(companyId, updatedCompany);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCompany, response.getBody());
    }

    @Test
    void testDeleteCompany() {
        Long companyId = 1L;
        Company company1 = company;

        ResponseEntity<Void> response = companyController.deleteCompany(companyId);

        verify(companyService).deleteCompany(companyId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetAllCompanies() {
        List<Company> companies = Arrays.asList(company, new Company());

        when(companyService.getAllCompanies()).thenReturn(companies);

        ResponseEntity<List<Company>> response = companyController.getAllCompanies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companies, response.getBody());
    }
}

