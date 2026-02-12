package com.freequestions.questions_api.controllers.user;

import com.freequestions.questions_api.config.auth.JwtAuthenticationFilter;
import com.freequestions.questions_api.dtos.user.CreateUserDTO;
import com.freequestions.questions_api.exceptions.EmailAlreadyInUseException;
import com.freequestions.questions_api.models.user.Role;
import com.freequestions.questions_api.models.user.User;
import com.freequestions.questions_api.services.user.UserService;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        JwtAuthenticationFilter jwtAuthenticationFilter() {
            return org.mockito.Mockito.mock(JwtAuthenticationFilter.class);
        }
    }

    @Test
    void shouldReturn201WhenUserIsCreated() throws Exception {
        CreateUserDTO dto = new CreateUserDTO(
          "student@email.com",
          "StrongP@ssw0rd1",
          "John",
                "Doe"
        );

        User fakeUser = new User(
                dto.getEmail(),
                "hashed-password",
                Role.STUDENT,
                dto.getName(),
                dto.getLastName()
        );

        when(userService.create(any(CreateUserDTO.class)))
                .thenReturn(fakeUser);

        mockMvc.perform(post("/api/users/createUser")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenDtoIsInvalid() throws Exception {
        CreateUserDTO dto = new CreateUserDTO(
          "invalid-email",
          "123",
          "",
                ""
        );

        mockMvc.perform(post("/api/users/createUser")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        CreateUserDTO dto = new CreateUserDTO(
                "student@email.com",
                "StrongP@ssw0rd1",
                "John",
                "Doe"
        );

        doThrow(new EmailAlreadyInUseException(dto.getEmail()))
                .when(userService).create(any(CreateUserDTO.class));

        mockMvc.perform(post("/api/users/createUser")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}
