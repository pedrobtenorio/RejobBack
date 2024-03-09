package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.*;
import com.efjpr.rejob.domain.Enums.EducationLevel;
import com.efjpr.rejob.domain.Enums.Role;
import com.efjpr.rejob.domain.Enums.SentenceRegime;
import com.efjpr.rejob.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    private UserService userService;

    MockMvc mockMvc;


    private Long userId = 1L;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
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

        Employee employee = Employee.builder()
                .id(1L)
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
    public void testGetEmployeeById() throws Exception {

        mockMvc.perform(get("/api/v1/users/{id}/employee", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(userService).getEmployee(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetCompanyById() throws Exception {

        mockMvc.perform(get("/api/v1/users/{id}/company", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(userService).getCompany(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetCollaboratorById() throws Exception {

        mockMvc.perform(get("/api/v1/users/{id}/collaborator", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(userService).getCollaborator(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testUpdateUserReturnsUpdatedUser() {
        Long id = 1L;
        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setPhoneNumber("098-765-4321");

        when(userService.updateUser(id, updatedUser)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(id, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;

        ResponseEntity<Void> response = userController.deleteUser(id);

        verify(userService).deleteUser(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}