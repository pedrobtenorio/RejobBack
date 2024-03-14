package com.efjpr.rejob.service;
import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.CollaboratorRegisterRequest;
import com.efjpr.rejob.domain.User;
import com.efjpr.rejob.repository.CollaboratorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.efjpr.rejob.domain.Enums.CollaboratorType.ONG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CollaboratorServiceTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CollaboratorService collaboratorService;

    public CollaboratorServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCollaborator() {
        CollaboratorRegisterRequest request = new CollaboratorRegisterRequest();
        request.setCompanyId(1L);
        request.setJobTitle("Job Title");
        request.setCollaboratorType(ONG);

        User user = new User();

        Company company = new Company();
        when(companyService.getCompanyById(1L)).thenReturn(company);

        collaboratorService.create(request, user);

        verify(collaboratorRepository, times(1)).save(any());
        verify(companyService, times(1)).setUserToCompany(any(), any());
    }

    @Test
    void testFindById() {
        Collaborator collaborator = new Collaborator();
        when(collaboratorRepository.findById(1L)).thenReturn(java.util.Optional.of(collaborator));

        Collaborator result = collaboratorService.findById(1L);

        assertEquals(collaborator, result);
    }

    @Test
    void testFindByUser() {
        User user = new User();
        Collaborator collaborator = new Collaborator();
        when(collaboratorRepository.findByUser(user)).thenReturn(java.util.Optional.of(collaborator));

        Collaborator result = collaboratorService.findByUser(user);

        assertEquals(collaborator, result);
    }
}
