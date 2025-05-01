package com.paulopsms.domain.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@SpringBootConfiguration
public class UserTest {

    @Test
    public void givenAName_whenCreateNewUser_thenShouldReturnUserId() {
        User user = new User(1L, "Paulo Sérgio");

        assertEquals(1, user.getId());
        assertEquals("Paulo Sérgio", user.getName());
    }

    @Test
    public void givenAnEmptyName_whenCreateNewUser_thenShouldThrowAnException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new User(1L, "");
        });

        String expectedMessage = "O nome do usuário é obrigatório.";
        String message = exception.getMessage();

        assertEquals(expectedMessage, message);
    }
}
