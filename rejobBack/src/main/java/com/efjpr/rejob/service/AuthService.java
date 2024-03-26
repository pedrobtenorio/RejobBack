package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.*;
import com.efjpr.rejob.domain.Enums.Role;
import com.efjpr.rejob.domain.User;
import com.efjpr.rejob.repository.UserRepository;
import com.efjpr.rejob.service.email.EmailService;
import com.efjpr.rejob.service.email.EmailServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CollaboratorService collaboratorService;
    private final EmployeeService employeeService;
    private final EmailServiceImpl emailService;
    private final CompanyService companyService;
    @Transactional
    public AuthResponse register(CollaboratorRegisterRequest request) {
        User user = createUser(request.getEmail(), request.getName(), request.getPassword(), request.getPhoneNumber(), Role.COLLABORATOR);
        var token = jwtService.generateToken(user);

        collaboratorService.create(request, user);

        // funçao de enviar e-mail de boas-vindas para Colaborador
        // Context context = new Context();
        // emailService.sendEmailWithHtmlTemplate(user.getEmail(), "Boas-Vindas", "bemvindo_colaborador.html", context);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
    @Transactional
    public AuthResponseCompany register(CompanyRegisterRequest request) {
        User user = createUser(request.getEmail(), request.getName(), request.getPassword(), request.getPhone(), Role.COMPANY);
        var token = jwtService.generateToken(user);

      Company company =  companyService.create(request, user);


        return AuthResponseCompany.builder()
                .token(token)
                .company(company)
                .build();
    }
    @Transactional
    public AuthResponse register(EmployeeRegisterRequest request) {

        User user = createUser(request.getEmail(), request.getName(), request.getPassword(), request.getPhoneNumber(), Role.USER);
        var token = jwtService.generateToken(user);

        employeeService.create(request, user);

        // funçao de enviar e-mail de boas-vindas para Egresso
        // Context context = new Context();
        // emailService.sendEmailWithHtmlTemplate(user.getEmail(), "Boas-Vindas", "bemvindo_empregado.html", context);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();


        var token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    private User createUser(String email, String name, String password, String phoneNumber, Role type) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }

        User user = User.builder()
                .name(name)
                .password(passwordEncoder.encode(password))
                .role(type)
                .email(email)
                .phoneNumber(phoneNumber)
                .createdDate(Date.from(Instant.now()))
                .lastUpdatedDate(Date.from(Instant.now()))
                .build();

        //handleProfilePicture(user, file);

       return userRepository.save(user);
    }

    private void handleProfilePicture(User user, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            if (!Arrays.asList("image/jpeg", "image/png", "image/gif", "image/jpg").contains(file.getContentType())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file format. Only JPEG, PNG, and GIF are allowed.");
            }

            try {
                byte[] profilePicBytes = file.getBytes();

                String base64EncodedProfilePic = Base64.getEncoder().encodeToString(profilePicBytes);

                user.setProfilePic(base64EncodedProfilePic);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the profile picture.");
            }
        }
    }

}
