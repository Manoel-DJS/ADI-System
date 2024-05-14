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
import tech.buildruin.agregadorInv.controller.dto.CreateUserDto;
import tech.buildruin.agregadorInv.controller.dto.UpdateUserDto;
import tech.buildruin.agregadorInv.entity.User;
import tech.buildruin.agregadorInv.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

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
        @DisplayName("Should get user by id with success when optinoal is present")
        void shouldGetByIdWithSuccessOptionalIsPresent() {

            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user)).
                    when(userRepository).
                    findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

        }

        @Test
        @DisplayName("Should get user by id with success when optinoal is Empty")
        void shouldGetByIdWithSuccessOptionalIsEmpty() {

            // Arrange
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(userId.toString());

            // Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());

        }


    }

    @Nested
    class listUsers{
        @Test
        @DisplayName("Should return all users with success")
        void shouldReturnAllUsersWithSuccess() {

            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            var userList = List.of(user);
            doReturn(userList).
                    when(userRepository).
                    findAll();

            // Act
            var output = userService.ListUsers();

            // Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class deleteById{

        @Test
        @DisplayName("Should delete user with success when user exists")
        void shouldDeleteUserWithSuccessWhenUserExists() {

            // Arrange

            doReturn(true).
                    when(userRepository).
                    existsById(uuidArgumentCaptor.capture()); // id = 0

            doNothing().
                    when(userRepository).
                    deleteById(uuidArgumentCaptor.capture()); // id = 1
            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            // verify mockito
            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Should not delete user when user not exists")
        void shouldNotDeleteUserWhenUserNotExists() {

            // Arrange

            doReturn(false).
                    when(userRepository).
                    existsById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            // verify mockito
            verify(userRepository, times(1)).
                    existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }

    }

    @Nested
    class updateUserById{
        @Test
        @DisplayName("Should update user by id when user exists and username and password is filled")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordIsFilled() {

            // Arrange
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );

            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user)).
                    when(userRepository).
                    findById(uuidArgumentCaptor.capture());
            doReturn(user).
                    when(userRepository).
                    save(userArgumentCaptor.capture());

            // Act
            userService.uptadeUserById(user.getUserId().toString(),updateUserDto);

            // Assert
            assertEquals(user.getUserId(),uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).
                    findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).
                    save(user);
        }

        @Test
        @DisplayName("Should not update user when user not exists")
        void shouldNotUpdateUserWhenUserNotExists() {

            // Arrange
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );
            var userId = UUID.randomUUID();



            doReturn(Optional.empty()).
                    when(userRepository).
                    findById(uuidArgumentCaptor.capture());

            // Act
            userService.uptadeUserById(userId.toString(),updateUserDto);

            // Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());



            verify(userRepository, times(1)).
                    findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).
                    save(any());

        }

    }

}