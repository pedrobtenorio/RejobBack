package com.efjpr.rejob.service;
import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.CompanyRegisterRequest;
import com.efjpr.rejob.domain.Enums.CompanyType;
import com.efjpr.rejob.domain.Enums.Role;
import com.efjpr.rejob.domain.Location;
import com.efjpr.rejob.domain.User;
import com.efjpr.rejob.repository.CollaboratorRepository;
import com.efjpr.rejob.repository.CompanyRepository;
import com.efjpr.rejob.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private CollaboratorService collaboratorService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    User user;

    @InjectMocks
    private CompanyService companyService;

    MockMvc mockMvc;

    public CompanyServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyService)
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
                .companyType(CompanyType.EMPRESA_COMERCIAL)
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
    void testCreateCompany() {
        CompanyRegisterRequest request = new CompanyRegisterRequest();
        User user = new User();
        Company expectedCompany = new Company();
        expectedCompany.setUser(user);

        when(companyRepository.save(any(Company.class))).thenReturn(expectedCompany);

        Company company = companyService.create(request, user);

        verify(companyRepository, times(1)).save(any(Company.class));
        assertEquals(user, company.getUser());
    }


    @Test
    void testGetCompanyById() {
        Company company = new Company();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Company result = companyService.getCompanyById(1L);

        assertEquals(company, result);
    }

    @Test
    void testFindByUser() {
        User user = new User();
        Company company = new Company();
        when(companyRepository.findByUser(user)).thenReturn(Optional.of(company));

        Company result = companyService.findByUser(user);

        assertEquals(company, result);
    }

    @Test
    void testSetUserToCompany() {
        Collaborator collaborator = new Collaborator();
        Company company = new Company();
        companyService.setUserToCompany(collaborator, company);

        verify(companyRepository, times(1)).save(company);
        assertEquals(1, company.getCollaborators().size());
    }

    @Test
    void testUpdateCompany() {
        Company existingCompany = new Company();
        Company updatedCompany = new Company();
        updatedCompany.setName("Updated Company");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(existingCompany)).thenReturn(existingCompany);

        Company result = companyService.updateCompany(1L, updatedCompany);

        assertEquals(updatedCompany.getName(), result.getName());
    }

    @Test
    void testUpdateCompanyNotFound() {
        Company updatedCompany = new Company();
        updatedCompany.setName("Updated Company");

        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> companyService.updateCompany(1L, updatedCompany));
    }

    @Test
    void testDeleteCompany() {
        Company companyToDelete = new Company();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(companyToDelete));

        companyService.deleteCompany(1L);

        verify(companyRepository, times(1)).delete(companyToDelete);
    }

    @Test
    void testDeleteCompanyNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> companyService.deleteCompany(1L));
    }

    @Test
    void testGetCompanyByCollaboratorId() {
        Collaborator collaborator = new Collaborator();
        collaborator.setCompany(new Company());

        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));

        Company result = companyService.getCompanyByCollaboratorId(1L);

        assertNotNull(result);
    }

    @Test
    void testGetCompanyByCollaboratorIdCollaboratorNotFound() {
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> companyService.getCompanyByCollaboratorId(1L));
    }

    @Test
    void testGetCompanyByCollaboratorIdCompanyNotFound() {
        Collaborator collaborator = new Collaborator();
        collaborator.setCompany(null);

        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));

        assertThrows(ResponseStatusException.class, () -> companyService.getCompanyByCollaboratorId(1L));
    }

    @Test
    void testGetAllCompanies() {
        List<Company> companies = new ArrayList<>();
        companies.add(new Company());
        companies.add(new Company());
        companies.add(new Company());

        when(companyRepository.findAll()).thenReturn(companies);

        List<Company> result = companyService.getAllCompanies();

        assertEquals(companies.size(), result.size());
    }

}
