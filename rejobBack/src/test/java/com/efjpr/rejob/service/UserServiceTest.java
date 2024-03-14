package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.Enums.Role;
import com.efjpr.rejob.domain.User;
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

    User user;

    @BeforeEach
    public void setUp() {
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
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getUserById(id);

        assertEquals(id, result.getId());
    }

    @Test
    void testGetCollaborator() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Collaborator collaborator = new Collaborator();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(collaboratorService.findByUser(user)).thenReturn(collaborator);

        Collaborator result = userService.getCollaborator(id);

        assertEquals(collaborator, result);
    }

    @Test
    void testGetEmployee() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Employee employee = new Employee();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(employeeService.findByUser(user)).thenReturn(employee);

        Employee result = userService.getEmployee(id);

        assertEquals(employee, result);
    }

    @Test
    void testGetCompany() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Company company = new Company();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(companyService.findByUser(user)).thenReturn(company);

        Company result = userService.getCompany(id);

        assertEquals(company, result);
    }

    @Test
    void testUpdateUser() {
        Long id = 1L;
        User existingUser = new User();
        User updatedUser = new User();
        updatedUser.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        User result = userService.updateUser(id, updatedUser);

        assertEquals(id, result.getId());
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;
        User existingUser = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(id);

        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    void testUpdateUserNotFound() {
        Long id = 1L;
        User updatedUser = new User();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(id, updatedUser));
    }

    @Test
    void testDeleteUserNotFound() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(id));
    }

}
