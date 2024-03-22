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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private User user;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .alwaysDo(print())
                .build();

        user = User.builder()
                .id(userId)
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
        User user1 = user;
        User user2 = new User(2L, "João Marcos", Role.COLLABORATOR, "joaomarcos@example.com", "senha123", "456-123-7890", new Date(2000, Calendar.JANUARY, 1), new Date(2000, Calendar.JANUARY, 1), "profile_pic_url");
        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testGetUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        // Configura o SecurityContextHolder para retornar o SecurityContext mockado
        SecurityContextHolder.setContext(securityContext);
        // Configura o SecurityContext mockado para retornar o Authentication mockado
        when(securityContext.getAuthentication()).thenReturn(authentication);
        // Configura o Authentication mockado para retornar o usuário
        when(authentication.getPrincipal()).thenReturn(user);

        // Chama o método a ser testado
        User result = userController.getUser();

        // Verifica se o usuário retornado é o mesmo configurado
        assertEquals(user, result);
    }

    @Test
    void testGetUserNotUserPrincipal() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new Object());

        assertThrows(IllegalStateException.class, () -> userController.getUser());
    }



    @Test
    public void testGetEmployeeByUserId() throws Exception {

        mockMvc.perform(get("/api/v1/users/{id}/employee", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(userService).getEmployee(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetCompanyByUserId() throws Exception {

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
    public void testUpdateUser() {
        User updatedUser = user;
        updatedUser.setPhoneNumber("098-765-4321");

        when(userService.updateUser(userId, updatedUser)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(userId, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void testDeleteUser() {

        ResponseEntity<Void> response = userController.deleteUser(userId);

        verify(userService).deleteUser(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
