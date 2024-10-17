package dev.timothewt.basicauthbackend.controllers;

import dev.timothewt.basicauthbackend.models.User;
import dev.timothewt.basicauthbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUser() {
        User currentUser = userService.getAuthenticatedUser();
        return ResponseEntity.ok(currentUser);
    }
}
