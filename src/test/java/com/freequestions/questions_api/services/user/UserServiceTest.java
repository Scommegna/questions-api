package com.freequestions.questions_api.services.user;

import com.freequestions.questions_api.dtos.user.CreateUserDTO;
import com.freequestions.questions_api.exceptions.EmailAlreadyInUseException;
import com.freequestions.questions_api.models.user.Role;
import com.freequestions.questions_api.models.user.User;
import com.freequestions.questions_api.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private CreateUserDTO dto;

    @BeforeEach
    void setUp() {
        dto = new CreateUserDTO(
                "student@email.com",
                "StrongP@ssw0rd1",
                "Student",
                "Test"
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.create(dto);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getRole()).isEqualTo(Role.STUDENT);
        assertThat(user.getPassword()).isEqualTo("hashed-password");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(dto))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessageContaining(dto.getEmail());

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }
}
