package ua.epam.mishchenko.ticketbooking.utils;

import ua.epam.mishchenko.ticketbooking.model.User;

public class Util {


    /**
     * Validates that a User object is not null and has required fields.
     * @param user User object to validate.
     * @throws IllegalArgumentException if user is null or invalid.
     */
    public static void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("User email cannot be null, empty, or blank");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("User name cannot be null, empty, or blank");
        }
    }

    /**
     * Validates that the given resource is not null.
     *
     * @param resource the object to validate
     * @param resourceName the name of the resource (for error messages)
     * @param <T> the type of the resource
     * @return the validated resource (non-null)
     * @throws IllegalArgumentException if the resource is null
     */
    public static <T> void validateNotNull(T resource, String resourceName) {
        if (resource == null) {
            throw new IllegalArgumentException(resourceName + " cannot be null");
        }
    }

    /**
     * Validates that an email is not null, not blank, and has a simple valid format.
     *
     * @param email Email string to validate.
     * @throws IllegalArgumentException if email is null, blank, or invalid.
     */
    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null, empty, or blank");
        }
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email is not valid: " + email);
        }
    }

    public static void validateString(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null, empty, or blank");
        }
    }


    /**
     * Validates that a long ID is positive.
     * @param id ID to validate.
     * @throws IllegalArgumentException if id <= 0.
     */
    public static void validateId(long id) {
        validateLong(id);
    }

    public static void validateLong(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be positive. Provided: " + value);
        }
    }

    /**
     * Validates pagination parameters.
     * @param pageSize number of items per page.
     * @param pageNum page number (starts from 1).
     * @throws IllegalArgumentException if parameters are invalid.
     */
    public static void validatePagination(int pageSize, int pageNum) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be positive. Provided: " + pageSize);
        }
        if (pageNum <= 0) {
            throw new IllegalArgumentException("Page number must be positive. Provided: " + pageNum);
        }
    }
}
