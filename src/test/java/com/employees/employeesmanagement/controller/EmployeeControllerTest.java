package com.employees.employeesmanagement.controller;

import com.employees.employeesmanagement.model.Employee;
import com.employees.employeesmanagement.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private Employee employee;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1L);
        employee.setName("Anas");
        employee.setEmail("anass_hamzaoui@hotmail.com");
        employee.setPosition("Admin");

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Anas"))
                .andExpect(jsonPath("$[0].email").value("anass_hamzaoui@hotmail.com"));

        verify(employeeService).getAllEmployees();
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anas"))
                .andExpect(jsonPath("$.email").value("anass_hamzaoui@hotmail.com"));

        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    public void testCreateEmployee() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);
        String employeeJson = objectMapper.writeValueAsString(employee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Anas"))
                .andExpect(jsonPath("$.email").value("anass_hamzaoui@hotmail.com"));

        verify(employeeService).createEmployee(any(Employee.class));
    }

}
