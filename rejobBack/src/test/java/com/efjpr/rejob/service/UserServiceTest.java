package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.*;
import com.efjpr.rejob.domain.Enums.*;
import com.efjpr.rejob.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    private CollaboratorService collaboratorService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private CompanyService companyService;

    private User user;

    private Company company;

    private Collaborator collaborator;

    private Employee employee;

    private Long id = 1L;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(id)
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
                .id(id)
                .companyType(CompanyType.PRIVATE_ENTERPRISE)
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
                .id(id)
                .user(user)
                .jobTitle("Cozinheiro")
                .collaboratorType(CollaboratorType.PRIVATE_ENTERPRISE)
                .company(company)
                .build();

        employee = Employee.builder()
                .id(id)
                .user(user)
                .cpf("12345678900")
                .prisonCode("ABC123")
                .educationLevel(EducationLevel.DOUTORADO_COMPLETO)
                .dateOfBirth("1990, 5, 15")
                .residenceLocation(new Location("Maceio", "Alagoas", "Vergel"))
                .sentenceRegime(SentenceRegime.ABERTO)
                .professionalExperience("5 years")
                .areasOfInterest("Programming, Technology")
                .skillsAndQualifications("Java, Spring Boot, SQL")
                .educationalHistory("Bachelor's Degree in Computer Science")
                .build();
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.getAllUsers();

        assertEquals(Collections.singletonList(user), users);
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testGetUserById() {

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getUserById(id);

        assertEquals(id, result.getId());
    }

  /*  @Test
    void testGetCollaborator() {

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(collaboratorService.findByUser(user)).thenReturn(collaborator);

        Collaborator result = userService.getCollaborator(id);

        assertEquals(collaborator, result);
    }*/

    @Test
    void testGetEmployee() {

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(employeeService.findByUser(user)).thenReturn(employee);

        Employee result = userService.getEmployee(id);

        assertEquals(employee, result);
    }

    @Test
    void testGetCompany() {

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(companyService.findByUser(user)).thenReturn(company);

        Company result = userService.getCompany(id);

        assertEquals(company, result);
    }

    @Test
    void testUpdateUser() {
        User updatedUser = user;
        updatedUser.setPhoneNumber("321-654-987");

        when(userRepository.findById(id)).thenReturn(Optional.of(updatedUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(id, updatedUser);

        assertEquals(id, result.getId());
    }

    @Test
    void testDeleteUser() {
        User existingUser = user;

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(id);

        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    void testUpdateUserNotFound() {
        Long id = 2L;
        User updatedUser = user;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(id, updatedUser));
    }

    @Test
    void testDeleteUserNotFound() {
        Long id = 2L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(id));
    }

}
