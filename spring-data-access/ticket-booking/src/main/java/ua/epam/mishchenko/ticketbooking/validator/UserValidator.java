package ua.epam.mishchenko.ticketbooking.validator;

import org.springframework.stereotype.Component;
import ua.epam.mishchenko.ticketbooking.model.impl.User;

@Component
public class UserValidator {


    public void validate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        validateEmail(user.getEmail());
        validateString(user.getName(), "User name");
    }

    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null, empty, or blank");
        }
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email is not valid: " + email);
        }
    }

    public void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null, empty, or blank");
        }
    }
}
