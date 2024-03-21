package com.efjpr.rejob.service;
import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.CompanyRegisterRequest;
import com.efjpr.rejob.domain.Enums.CollaboratorType;
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

    @InjectMocks
    private CompanyService companyService;

    MockMvc mockMvc;

    private User user;

    private Company company;

    private Collaborator collaborator;

    public CompanyServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyService)
                .alwaysDo(print())
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
                .businessActivity("Agricultura")
                .phone("123-456-7890")
                .email("company@example.com")
                .institutionalDescription("Description")
                .numberOfEmployees(100)
                .user(user)
                .headquarters(new Location("Maceio", "Alagoas", "Vergel"))
                .name("Verde Cultura")
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
    void testCreateCompany() {
        CompanyRegisterRequest request = new CompanyRegisterRequest();
        Company expectedCompany = company;
        expectedCompany.setUser(user);

        when(companyRepository.save(any(Company.class))).thenReturn(expectedCompany);

        Company company = companyService.create(request, user);

        verify(companyRepository, times(1)).save(any(Company.class));
        assertEquals(user, company.getUser());
    }


    @Test
    void testGetCompanyById() {
        Company company1 = company;
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Company result = companyService.getCompanyById(1L);

        assertEquals(company1, result);
    }

    @Test
    void testFindByUser() {
        User user1 = user;
        Company company1 = company;
        company.setUser(user1);

        when(companyRepository.findByUser(user)).thenReturn(Optional.of(company));

        Company result = companyService.findByUser(user);

        assertEquals(company1, result);
    }

    @Test
    void testSetUserToCompany() {
        Collaborator collaborator1 = collaborator;
        Company company1 = company;
        companyService.setUserToCompany(collaborator1, company1);

        verify(companyRepository, times(1)).save(company);
        assertEquals(1, company.getCollaborators().size());
    }

    @Test
    void testUpdateCompany() {
        Company updatedCompany = company;
        updatedCompany.setName("Updated Company Name");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(updatedCompany));
        when(companyRepository.save(updatedCompany)).thenReturn(updatedCompany);

        Company result = companyService.updateCompany(1L, updatedCompany);

        assertEquals(updatedCompany.getName(), result.getName());
    }

    @Test
    void testUpdateCompanyNotFound() {
        Company updatedCompany = company;

        when(companyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> companyService.updateCompany(2L, updatedCompany));
    }

    @Test
    void testDeleteCompany() {
        Company companyToDelete = company;
        when(companyRepository.findById(1L)).thenReturn(Optional.of(companyToDelete));

        companyService.deleteCompany(1L);

        verify(companyRepository, times(1)).delete(companyToDelete);
    }

    @Test
    void testDeleteCompanyNotFound() {
        when(companyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> companyService.deleteCompany(2L));
    }

    @Test
    void testGetCompanyByCollaboratorId() {
        Collaborator collaborator1 = collaborator;
        collaborator.setCompany(company);

        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator1));

        Company result = companyService.getCompanyByCollaboratorId(1L);

        assertNotNull(result);
    }

    @Test
    void testGetCompanyByCollaboratorIdCollaboratorNotFound() {
        when(collaboratorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> companyService.getCompanyByCollaboratorId(2L));
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
        Company company1 = company;

        Company company2 = new Company(2L, "22222222222222", "Secos e Frios", "Mercado", 50, new Location("Maceio", "Alagoas", "Benedito Bentes"), "222-222-2222", "Mercado especializado em secos, queijos e vinhos", "company@example.com", CompanyType.EMPRESA_COMERCIAL, user, null);

        List<Company> companies = Arrays.asList(company1, company2);

        when(companyRepository.findAll()).thenReturn(companies);

        List<Company> result = companyService.getAllCompanies();

        assertEquals(companies.size(), result.size());
    }

}
