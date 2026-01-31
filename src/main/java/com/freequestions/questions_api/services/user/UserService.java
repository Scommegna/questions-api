package com.freequestions.questions_api.services.user;

import com.freequestions.questions_api.dtos.user.CreateUserDTO;
import com.freequestions.questions_api.models.user.Role;
import com.freequestions.questions_api.models.user.User;
import com.freequestions.questions_api.repositories.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User create(CreateUserDTO dto) {
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User(
                dto.getEmail(),
                hashedPassword,
                Role.STUDENT,
                dto.getName(),
                dto.getLastName()
        );

        return userRepository.save(user);
    }
}
