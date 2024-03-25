package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.CollaboratorGetRequest;
import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.Enums.Role;
import com.efjpr.rejob.domain.User;
import com.efjpr.rejob.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(description = "Retorna todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos usuários retornados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(description = "Informações do usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "Não é um usuário")
    })
    @GetMapping("/me")
    public ResponseEntity<?>  getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User user) {
            Role role = user.getRole();
            return switch (role) {
                case USER -> new ResponseEntity<>(userService.getEmployee(user.getId()), HttpStatus.OK);
                case COMPANY -> new ResponseEntity<>(userService.getCompany(user.getId()), HttpStatus.OK);
                case COLLABORATOR -> new ResponseEntity<>(userService.getCollaborator(user.getId()), HttpStatus.OK);
                case ADMIN -> new ResponseEntity<>(user, HttpStatus.OK);
            };
        } else {
            throw new IllegalStateException("Authenticated principal is not a User");
        }
    }

    @Operation(description = "Busca funcionário por id e retorna")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário retornado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/{userId}/employee")
    public ResponseEntity<Employee> getEmployeeByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getEmployee(userId), HttpStatus.OK);
    }

    @Operation(description = "Retorna empresa pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/{userId}/company")
    public ResponseEntity<Company> getCompanyByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getCompany(userId), HttpStatus.OK);
    }

    @Operation(description = "Retorna colaborador pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaborador retornado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @GetMapping("/{userId}/collaborator")
    public ResponseEntity<CollaboratorGetRequest> getCollaboratorByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getCollaborator(userId),HttpStatus.OK);
    }

    @Operation(description = "Altera informações de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações atualizadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário com esse id foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return new ResponseEntity<>(userService.updateUser(id, updatedUser), HttpStatus.OK);
    }

    @Operation(description = "Deleta um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário com esse id foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Não autorizado / Token inválido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
