package com.freequestions.questions_api.services.auth;

import com.freequestions.questions_api.dtos.auth.LoginDTO;
import com.freequestions.questions_api.exceptions.InvalidCredentialsException;
import com.freequestions.questions_api.models.user.User;
import com.freequestions.questions_api.repositories.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return jwtService.generateToken(user);
    }
}
