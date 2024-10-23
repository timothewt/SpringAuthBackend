package dev.timothewt.basicauthbackend.controllers;

import dev.timothewt.basicauthbackend.dto.AuthenticationRequest;
import dev.timothewt.basicauthbackend.dto.AuthenticationResponse;
import dev.timothewt.basicauthbackend.dto.RegisterRequest;
import dev.timothewt.basicauthbackend.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegisterRequest request,
        HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.register(request, response));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request,
        HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request, response));
    }

    @GetMapping("/refresh")
    public void refreshToken(
        HttpServletResponse response,
        HttpServletRequest request
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public void logout(
        HttpServletResponse response,
        HttpServletRequest request
    ) {
        authenticationService.logout(request, response);
    }
}
