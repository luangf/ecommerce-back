package com.talkovia.services;

import com.talkovia.customexceptions.UserAlreadyExistsException;
import com.talkovia.dto.auth.LoginRequestDTO;
import com.talkovia.dto.auth.RegisterRequestDTO;
import com.talkovia.model.User;
import com.talkovia.repositories.UserRepository;
import com.talkovia.security.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public String login(LoginRequestDTO loginRequestDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        User user = (User) auth.getPrincipal();
        return tokenService.generateToken(user);
    }

    public String register(RegisterRequestDTO registerRequestDTO) {
        boolean emailExists = userRepository.existsByEmail(registerRequestDTO.email());
        boolean usernameExists = userRepository.existsByUsername(registerRequestDTO.username());

        if(emailExists || usernameExists){
            throw new UserAlreadyExistsException("Email or username already in use");
        }

        User newUser = new User(
                registerRequestDTO.email(),
                registerRequestDTO.username(),
                passwordEncoder.encode(registerRequestDTO.password())
        );
        userRepository.save(newUser);
        return tokenService.generateToken(newUser);
    }
}
