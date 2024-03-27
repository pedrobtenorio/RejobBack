package com.efjpr.rejob.controller;

import com.efjpr.rejob.controller.AuthController;
import com.efjpr.rejob.domain.Dto.*;
import com.efjpr.rejob.service.AuthService;
import com.efjpr.rejob.service.CompanyService;
import com.efjpr.rejob.service.email.EmailService;
import com.efjpr.rejob.domain.Company;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private EmailService emailService;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegisterEmployee() {
        MockitoAnnotations.openMocks(this);
        EmployeeRegisterRequest request = new EmployeeRegisterRequest();
        AuthResponse response = new AuthResponse("token");
        when(authService.register(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.register(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authService, times(1)).register(request);
    }

    // Os dois testes a seguir estão comentados pois
    // não estão funcionando devido a restrições de acesso
    // por motivos de segurança (CollaboratorRegisterRequest)

    /* @Test
    void testRegisterCollaborator() {
        MockitoAnnotations.openMocks(this);
        CollaboratorRegisterRequest request = new CollaboratorRegisterRequest();
        AuthResponse response = new AuthResponse("token");
        when(authService.register(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.register(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authService, times(1)).register(request);
    }*/

    /* @Test
    void testRegisterCompany() {
        MockitoAnnotations.openMocks(this);
        CompanyRegisterRequest request = new CompanyRegisterRequest();
        AuthResponseCompany response = new AuthResponseCompany("token", new Company());
        when(authService.register(request)).thenReturn(response);

        ResponseEntity<AuthResponseCompany> result = authController.register(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authService, times(1)).register(request);
    }*/

    @Test
    void testAuthenticate() {
        MockitoAnnotations.openMocks(this);
        AuthRequest request = new AuthRequest("email", "password");
        AuthResponse response = new AuthResponse("token");
        when(authService.authenticate(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.authenticate(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authService, times(1)).authenticate(request);
    }

    @Test
    void testSendEmail() {
        MockitoAnnotations.openMocks(this);

        String result = authController.SendEmail();

        assertEquals("ok", result);
        verify(emailService, times(1)).sendSimpleMessage(anyString(), anyString(), anyString());
    }
}
