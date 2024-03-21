package com.efjpr.rejob.service;
import com.efjpr.rejob.domain.*;
import com.efjpr.rejob.domain.Dto.CollaboratorRegisterRequest;
import com.efjpr.rejob.domain.Enums.*;
import com.efjpr.rejob.repository.CollaboratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;

import static com.efjpr.rejob.domain.Enums.CollaboratorType.ONG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollaboratorServiceTest {

    @InjectMocks
    private CollaboratorService collaboratorService;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private CompanyService companyService;

    private User user;

    private Company company;

    private Collaborator collaborator;

    @BeforeEach
    public void setUp() {
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
                .id(1L)
                .user(user)
                .jobTitle("Cozinheiro")
                .collaboratorType(CollaboratorType.PRIVATE_ENTERPRISE)
                .company(company)
                .build();

    }

    @Test
    void testCreateCollaborator() {
        CollaboratorRegisterRequest request = new CollaboratorRegisterRequest();
        request.setCompanyId(1L);
        request.setJobTitle("Vendedor");
        request.setCollaboratorType(ONG);

        User newUser = user;

        when(companyService.getCompanyById(1L)).thenReturn(company);

        collaboratorService.create(request, newUser);

        verify(collaboratorRepository, times(1)).save(any());
        verify(companyService, times(1)).setUserToCompany(any(), any());
    }

    @Test
    void testFindById() {
        Collaborator collaborator1 = collaborator;
        when(collaboratorRepository.findById(1L)).thenReturn(java.util.Optional.of(collaborator));

        Collaborator result = collaboratorService.findById(1L);

        assertEquals(collaborator1, result);
    }

    @Test
    void testFindByUser() {
        User user1 = user;
        Collaborator collaborator1 = collaborator;
        collaborator.setUser(user1);

        when(collaboratorRepository.findByUser(user)).thenReturn(java.util.Optional.of(collaborator));

        Collaborator result = collaboratorService.findByUser(user);

        assertEquals(collaborator1, result);
    }
}
