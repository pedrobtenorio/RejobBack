package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.*;
import com.efjpr.rejob.service.AuthService;
import com.efjpr.rejob.service.email.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final EmailService emailService;

    @Operation(description = "Registra egresso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Egresso registrado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PostMapping("/register-employee")
    public ResponseEntity<AuthResponse> register(@RequestBody EmployeeRegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.OK);
    }

    @Operation(description = "Registra colaborador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaborador registrado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PostMapping("/register-collaborator")
    public ResponseEntity<AuthResponse> register(@RequestBody CollaboratorRegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.OK);
    }

    @Operation(description = "Registra empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa registrada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PostMapping("/register-Company")
    public ResponseEntity<AuthResponseCompany> register(@RequestBody CompanyRegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.OK);
    }


    @Operation(description = "Autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return new ResponseEntity<>(authService.authenticate(request), HttpStatus.OK);
    }

    @GetMapping("/send-email")
    public String SendEmail() {
        emailService.sendSimpleMessage("fgogf@ic.ufal.br", "Teste", "Teste");
        return "ok";
    }

}
