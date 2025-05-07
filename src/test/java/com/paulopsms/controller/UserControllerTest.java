package com.paulopsms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.paulopsms.domain.model.User;
import com.paulopsms.exception.UserRuntimeException;
import com.paulopsms.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private UserController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static List<User> users;
    private static User userDto;
    private static User user;
    private static User createdUser;

    @BeforeAll
    public static void init() {
        users = new ArrayList<>();
        userDto = User.builder().name("John Doe").build();
        user = User.builder().id(1L).name("John Doe").build();
        createdUser = User.builder().id(1L).name("John Doe").build();
    }

    @Test
    public void assertThatControllerIsNotNull() {
        assertNotNull(controller);
    }

    @Test
    void givenAUserResource_whenListingUsers_thenShouldReturnAnEmptyList() throws Exception {
        when(userService.listUsers()).thenReturn(users);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        List<User> users = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertEquals(200, response.getStatus());
        assertTrue(users.isEmpty());
    }

    @Test
    public void givenAUserResource_whenCreatingNewUser_thenShouldReturnCreatedUser() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenReturn(createdUser);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        User createdUser = objectMapper.readValue(contentAsString, new TypeReference<User>() {
        });

        assertEquals(201, response.getStatus());
        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
        assertEquals("John Doe", createdUser.getName());
    }

    @Test
    public void givenUserId_whenFindingUser_thenShouldReturnUserById() throws Exception {
        when(userService.findUserById(1L)).thenReturn(user);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        User foundUser = objectMapper.readValue(contentAsString, new TypeReference<User>() {
        });

        assertEquals(200, response.getStatus());
        assertNotNull(foundUser);
        assertEquals(1, foundUser.getId());
        assertEquals("John Doe", foundUser.getName());
    }


    @Test
    public void givenUserId_whenDeletingUser_thenUserShouldBeDeleted() throws Exception {
        when(userService.removeUser(1L)).thenReturn(user);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        User removedUser = objectMapper.readValue(contentAsString, new TypeReference<User>() {
        });

        assertEquals(200, response.getStatus());
        assertNotNull(removedUser);
        assertEquals(1, removedUser.getId());
        assertEquals("John Doe", removedUser.getName());
        Mockito.verify(userService, Mockito.times(1)).removeUser(1L);
    }


    @Test
    public void givenAUserResource_withMissingUserName_thenShouldReturnBadRequestAndCaptureException() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenThrow(new UserRuntimeException("O nome do usuário é obrigatório."));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(User.builder().name("John Doe").build())))
                .andExpect(result -> assertInstanceOf(UserRuntimeException.class, result.getResolvedException()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        assertEquals(BAD_REQUEST.value(), response.getStatus());
        String contentAsString = response.getContentAsString();
        assertEquals("O nome do usuário é obrigatório.", contentAsString);
    }
}
