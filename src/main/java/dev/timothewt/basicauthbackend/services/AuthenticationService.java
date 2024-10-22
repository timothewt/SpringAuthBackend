package dev.timothewt.basicauthbackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.timothewt.basicauthbackend.config.JwtService;
import dev.timothewt.basicauthbackend.dto.AuthenticationRequest;
import dev.timothewt.basicauthbackend.dto.AuthenticationResponse;
import dev.timothewt.basicauthbackend.dto.RegisterRequest;
import dev.timothewt.basicauthbackend.models.Role;
import dev.timothewt.basicauthbackend.models.User;
import dev.timothewt.basicauthbackend.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${auth.passwordSalt}")
    private String passwordSalt;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt.refreshExpirationTimeMs}")
    private int REFRESH_EXPIRATION_TIME_MS;

    public AuthenticationResponse register(
        RegisterRequest request,
        HttpServletResponse response
    ) {
        User user  = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword() + passwordSalt))
            .role(Role.USER)
            .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        setRefreshTokenCookie(response, refreshToken);

        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(
        AuthenticationRequest request,
        HttpServletResponse response
    ) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword() + passwordSalt
            )
        );
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        setRefreshTokenCookie(response, refreshToken);

        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
    }

    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String refreshToken = null;

        if (request.getCookies() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refresh_token".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtService.extractUsername(refreshToken);

        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UserDetails userDetails = this.userRepository.findByUsername(username)
            .orElseThrow();

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String accessToken = jwtService.generateToken(userDetails);

        setRefreshTokenCookie(response, refreshToken);

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
            .accessToken(accessToken)
            .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }


    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(REFRESH_EXPIRATION_TIME_MS);
        response.addCookie(cookie);
    }
}
