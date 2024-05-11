package tech.buildruin.agregadorInv.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.buildruin.agregadorInv.controller.CreateUserDto;
import tech.buildruin.agregadorInv.entity.User;
import tech.buildruin.agregadorInv.repository.UserRepository;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // Teste Unitário no padrão triplolathi = Arrange - Act - Assert
    // Arrange = Arrumar/organizar tudo que o teste precisa
    // Act = Chamar o trecho que será testado
    // Assert = verificação

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Nested
    class createUser{

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUser(){

            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
                var input = new CreateUserDto(
                        "username",
                        "email@email.com",
                        "password"
                );
            // Act
            var output = userService.createUser(input);

            // Assert
            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName("Should Throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {

            // Arrange

            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
                    "username",
                    "email@email.com",
                    "password"
            );
            // Act - Assert
            assertThrows(RuntimeException.class, () ->  userService.createUser(input));

        }
    }

    @Nested
    class getUserById{

        @Test
        @DisplayName("Should get user by id with success")
        void shouldGetByIdWithSuccess() {

            // Arrange
            // Act
            // Assert

        }
    }
}