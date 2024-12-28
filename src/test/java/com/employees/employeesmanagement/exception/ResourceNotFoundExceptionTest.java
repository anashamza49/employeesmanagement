package com.employees.employeesmanagement.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testExceptionMessage() {
        String errorMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testExceptionMessageAndCause() {
        String errorMessage = "Resource not found";
        Throwable cause = new RuntimeException("Cause of the exception");
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage, cause);

        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
