package com.example.cloudServiceImpl.util;

import com.example.dto.SubscriptionRequestDto;
import com.example.dto.UserRequestDto;

import java.time.LocalDate;

public class Util {

    /**
     * Validates that a User object is not null and has required fields.
     * @param user User object to validate.
     * @throws IllegalArgumentException if user is null or invalid.
     */
    public static void validateUser(UserRequestDto user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        validateString(user.getName(), "User name");
        validateString(user.getSurname(), "User surname");
        validateBirthday(user.getBirthday());
    }

    /**
     * Validates that the given resource is not null.
     *
     * @param resource the object to validate
     * @param resourceName the name of the resource (for error messages)
     * @param <T> the type of the resource
     * @throws IllegalArgumentException if the resource is null
     */
    public static <T> void validateNotNull(T resource, String resourceName) {
        if (resource == null) {
            throw new IllegalArgumentException(resourceName + " cannot be null");
        }
    }

    /**
     * Validates that a string is not null, not empty, and not blank.
     *
     * @param value String to validate.
     * @param fieldName Name of the field for error messages.
     * @throws IllegalArgumentException if string is null, empty, or blank.
     */
    public static void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null, empty, or blank");
        }
    }

    /**
     * Validates that a birthday is not null and not in the future.
     *
     * @param birthday LocalDate to validate.
     * @throws IllegalArgumentException if birthday is null or in the future.
     */
    public static void validateBirthday(LocalDate birthday) {
        if (birthday == null) {
            throw new IllegalArgumentException("Birthday cannot be null");
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birthday cannot be in the future");
        }
    }

    /**
     * Validates that a long ID is positive.
     * @param id ID to validate.
     * @throws IllegalArgumentException if id is null or <= 0.
     */
    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be positive. Provided: " + id);
        }
    }

    /**
     * Validates pagination parameters.
     * @param pageSize number of items per page.
     * @param pageNum page number (starts from 1).
     * @throws IllegalArgumentException if parameters are invalid.
     */
    public static void validatePagination(int pageSize, int pageNum) {
        if (pageSize < 0) {
            throw new IllegalArgumentException("Page size must be positive. Provided: " + pageSize);
        }
        if (pageNum <= 0) {
            throw new IllegalArgumentException("Page number must be positive. Provided: " + pageNum);
        }
    }

    public static void validateSubscription(SubscriptionRequestDto subscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription cannot be null");
        }
        validateId(subscription.getUserId());
    }
}
