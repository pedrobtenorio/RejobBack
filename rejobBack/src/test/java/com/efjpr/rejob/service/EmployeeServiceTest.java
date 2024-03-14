package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.Dto.EmployeeRegisterRequest;
import com.efjpr.rejob.domain.Employee;
import com.efjpr.rejob.domain.User;
import com.efjpr.rejob.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    private final EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
    private final EmployeeService employeeService = new EmployeeService(employeeRepository);

    @Test
    void testCreateEmployee() {
        EmployeeRegisterRequest request = new EmployeeRegisterRequest();
        request.setCpf("12345678900");
        // Set other fields as needed

        User user = new User();
        user.setEmail("test@example.com");
        // Set other user fields as needed

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        employeeService.create(request, user);

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testFindEmployeeByUser() {
        User user = new User();
        user.setEmail("test@example.com");

        Employee employee = new Employee();
        employee.setUser(user);

        when(employeeRepository.findByUser(user)).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.findByUser(user);

        assertEquals(employee, foundEmployee);
    }

    @Test
    void testFindByUserNotFound() {
        User user = new User();
        user.setEmail("test@example.com");

        when(employeeRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> employeeService.findByUser(user));
    }
}
