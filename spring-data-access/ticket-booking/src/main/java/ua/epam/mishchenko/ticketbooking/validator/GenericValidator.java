package ua.epam.mishchenko.ticketbooking.validator;

import org.springframework.stereotype.Component;

@Component
public class GenericValidator {
    public void validateNotNull(Object resource, String resourceName) {
        if (resource == null) {
            throw new IllegalArgumentException(resourceName + " cannot be null");
        }
    }

    public void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null, empty, or blank");
        }
    }

    public void validateId(long id, String fieldName) {
        validateLong(id, fieldName);
    }

    public  void validateLong(long value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + "Value must be positive. Provided: " + value);
        }
    }

    public void validatePagination(int pageSize, int pageNum) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be positive. Provided: " + pageSize);
        }
        if (pageNum <= 0) {
            throw new IllegalArgumentException("Page number must be positive. Provided: " + pageNum);
        }
    }


    public void validateFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive. Provided: " + amount);
        }
        if (amount > Double.MAX_VALUE) {
            throw new IllegalArgumentException("Amount must not exceed " + Double.MAX_VALUE + ". Provided: " + amount);
        }
    }
}
