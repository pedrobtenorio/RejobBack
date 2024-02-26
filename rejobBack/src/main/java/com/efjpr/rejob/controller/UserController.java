package com.efjpr.rejob.controller;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.CollaboratorGetRequest;
import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.Enums.Role;
import com.efjpr.rejob.domain.User;
import com.efjpr.rejob.service.UserService;
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

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

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

    @GetMapping("/{userId}/employee")
    public ResponseEntity<Employee> getEmployeeByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getEmployee(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/company")
    public ResponseEntity<Company> getCompanyByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getCompany(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/collaborator")
    public ResponseEntity<CollaboratorGetRequest> getCollaboratorByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getCollaborator(userId),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return new ResponseEntity<>(userService.updateUser(id, updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
