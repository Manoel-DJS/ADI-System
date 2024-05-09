package tech.buildruin.agregadorInv.service;

import org.springframework.stereotype.Service;
import tech.buildruin.agregadorInv.controller.CreateUserDto;
import tech.buildruin.agregadorInv.entity.User;
import tech.buildruin.agregadorInv.repository.UserRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDto createUserDto){

        // DTO -> ENTITY

        var entity = new User(UUID.randomUUID(),
                    createUserDto.username(),
                    createUserDto.email(),
                    createUserDto.password(),
                    Instant.now(),
                null);

        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }
}
