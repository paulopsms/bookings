package com.paulopsms.controller;

import com.paulopsms.domain.model.User;
import com.paulopsms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody User createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }

    @GetMapping("/users")
    public @ResponseBody List<User> createUser() {
        return this.userService.listUsers();
    }

    @GetMapping("/users/{id}")
    public @ResponseBody User findUser(@PathVariable("id") Long userId) {
        return this.userService.findUserById(userId);
    }

    @DeleteMapping("/users/{id}")
    public @ResponseBody User removeUser(@PathVariable("id") Long userId) {
        return this.userService.removeUser(userId);
    }
}
