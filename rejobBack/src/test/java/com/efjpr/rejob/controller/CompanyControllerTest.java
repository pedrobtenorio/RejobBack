package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Enums.CompanyType;
import com.efjpr.rejob.domain.Enums.Role;
import com.efjpr.rejob.domain.Location;
import com.efjpr.rejob.domain.User;
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

    MockMvc mockMvc;

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


        Company company = Company.builder()
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

    }

    @Test
    void testGetCompanyById() {
        Long companyId = 1L;
        Company company = new Company();

        when(companyService.getCompanyById(companyId)).thenReturn(company);

        ResponseEntity<Company> response = companyController.getCompanyById(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(company, response.getBody());
    }

    @Test
    void testGetCompanyByCollaboratorId() {
        Long collaboratorId = 1L;
        Company company = new Company();

        when(companyService.getCompanyByCollaboratorId(collaboratorId)).thenReturn(company);

        ResponseEntity<Company> response = companyController.getCompanyByCollaboratorId(collaboratorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(company, response.getBody());
    }

    @Test
    void testGetCollaboratorListFromSameCompany() {
        Long collaboratorId = 1L;
        Company company = new Company();
        Collaborator collaborator = new Collaborator();

        when(companyService.getCompanyByCollaboratorId(collaboratorId)).thenReturn(company);

        ResponseEntity<List<Collaborator>> response = companyController.getCollaboratorListFromSameCompany(collaboratorId);

//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(List.of(collaborator), response.getBody());
    }

    @Test
    void testUpdateCompany() {
        Long companyId = 1L;
        Company company = new Company();

        when(companyService.updateCompany(companyId, company)).thenReturn(company);

        ResponseEntity<Company> response = companyController.updateCompany(companyId, company);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(company, response.getBody());
    }

    @Test
    void testDeleteCompany() {
        Long companyId = 1L;

        ResponseEntity<Void> response = companyController.deleteCompany(companyId);

        verify(companyService).deleteCompany(companyId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetAllCompanies() {
        List<Company> companies = Arrays.asList(new Company(), new Company());

        when(companyService.getAllCompanies()).thenReturn(companies);

        ResponseEntity<List<Company>> response = companyController.getAllCompanies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companies, response.getBody());
    }
}

