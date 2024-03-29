package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.CollaboratorGetRequest;
import com.efjpr.rejob.domain.Dto.CollaboratorRegisterRequest;
import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.User;
import com.efjpr.rejob.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CollaboratorService collaboratorService;
    private final EmployeeService employeeService;
    private final CompanyService companyService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }

    public User getUserById(Long id) {
      return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found"));
    }

    public CollaboratorGetRequest getCollaborator(Long id) {
        User user = getUserById(id);
        Collaborator collaborator = this.collaboratorService.findByUser(user);

        return CollaboratorGetRequest.builder()
                .user(collaborator.getUser())
                .collaboratorType(collaborator.getCollaboratorType())
                .jobTitle(collaborator.getJobTitle())
                .companyId(collaborator.getCompanyId())
                .collaboratorId(collaborator.getId())
                .build();
    }

    public Employee getEmployee(Long id) {
        User user = getUserById(id);
        return this.employeeService.findByUser(user);
    }

    public Company getCompany(Long id) {
        User user = getUserById(id);
        return this.companyService.findByUser(user);
    }


    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found") {
                });
        validateAndApplyUpdates(existingUser, updatedUser);
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found"));
        userRepository.delete(existingUser);
    }

    private void validateAndApplyUpdates(User existingUser, User updatedUser) {
        existingUser.setRole(updatedUser.getRole());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setLastUpdatedDate(updatedUser.getLastUpdatedDate());
        existingUser.setProfilePic(updatedUser.getProfilePic());
    }

}