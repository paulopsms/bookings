package com.paulopsms.validation;

import com.paulopsms.domain.model.User;
import com.paulopsms.exception.ValidationRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserValidationServiceTest {

    @Autowired
    private UserValidationService userValidationService;


    @Test
    public void givenAnEmptyUserModelName_whenValidatingUserWithoutName_thenShouldThrowAnException() {
        RuntimeException exception = assertThrows(ValidationRuntimeException.class, () -> {
            userValidationService.validateUser(new User());
        });

        String expectedMessage = "O nome do usuário é obrigatório.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }


    @Test
    public void givenAValidUser_whenValidatingUser_thenShouldPassWithoutException() {
        User user = User.builder().name("Valid User").build();

        userValidationService.validateUser(user);
    }
}
