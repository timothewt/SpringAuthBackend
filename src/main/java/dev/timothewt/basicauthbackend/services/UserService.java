package dev.timothewt.basicauthbackend.services;

import dev.timothewt.basicauthbackend.exceptions.AuthenticationException;
import dev.timothewt.basicauthbackend.exceptions.UserNotFoundException;
import dev.timothewt.basicauthbackend.models.User;
import dev.timothewt.basicauthbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new AuthenticationException("No authenticated user");

        User authenticatedUser = (User) authentication.getPrincipal();

        if (userRepository.findByUsername(authenticatedUser.getUsername()).isEmpty())
            throw new UserNotFoundException("User not found");

        authenticatedUser.setPassword(null);

        return authenticatedUser;
    }

}
