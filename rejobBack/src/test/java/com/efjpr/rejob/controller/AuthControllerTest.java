package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Dto.AuthRequest;
import com.efjpr.rejob.domain.Dto.AuthResponse;
import com.efjpr.rejob.domain.Dto.AuthResponseCompany;
import com.efjpr.rejob.domain.Dto.CollaboratorRegisterRequest;
import com.efjpr.rejob.domain.Dto.CompanyRegisterRequest;
import com.efjpr.rejob.domain.Dto.EmployeeRegisterRequest;
import com.efjpr.rejob.service.AuthService;
import com.efjpr.rejob.service.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegisterEmployee() {
        EmployeeRegisterRequest request = new EmployeeRegisterRequest();
        AuthResponse response = new AuthResponse();

        when(authService.register(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.register(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testRegisterCollaborator() {
        CollaboratorRegisterRequest request = new CollaboratorRegisterRequest();
        AuthResponse response = new AuthResponse();

        when(authService.register(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.register(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

//    @Test
//    void testRegisterCompany() {
//        CompanyRegisterRequest request = new CompanyRegisterRequest();
//        AuthResponseCompany response = new AuthResponseCompany();
//
//        when(authService.register(request)).thenReturn(response);
//
//        ResponseEntity<AuthResponseCompany> result = authController.register(request);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(response, result.getBody());
//    }

    @Test
    void testAuthenticate() {
        AuthRequest request = new AuthRequest();
        AuthResponse response = new AuthResponse();

        when(authService.authenticate(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.authenticate(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testSendEmail() {
        String result = authController.SendEmail();

        verify(emailService).sendSimpleMessage("fgogf@ic.ufal.br", "Teste", "Teste");
        assertEquals("ok", result);
    }
}
