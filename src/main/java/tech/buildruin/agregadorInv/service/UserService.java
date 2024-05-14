package tech.buildruin.agregadorInv.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.buildruin.agregadorInv.controller.dto.CreateAccountDto;
import tech.buildruin.agregadorInv.controller.dto.CreateUserDto;
import tech.buildruin.agregadorInv.controller.dto.UpdateUserDto;
import tech.buildruin.agregadorInv.entity.Account;
import tech.buildruin.agregadorInv.entity.BillingAddress;
import tech.buildruin.agregadorInv.entity.User;
import tech.buildruin.agregadorInv.repository.AccountRepository;
import tech.buildruin.agregadorInv.repository.BillingAddressRepository;
import tech.buildruin.agregadorInv.repository.UserRepository;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository,AccountRepository accountRepository,
                       BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
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

    public Optional<User> getUserById(String userId){

        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> ListUsers(){
        return userRepository.findAll();
    }

    public void uptadeUserById(String userId,
                               UpdateUserDto updateUserDto){
        var id = UUID.fromString(userId);

        var userEntity = userRepository.findById(id);

        if(userEntity.isPresent()){
            var user = userEntity.get();

            if (updateUserDto.username() != null){
                user.setUsername(updateUserDto.username());
            }

            if(updateUserDto.password() != null){
                user.setPassword(updateUserDto.password());
            }
            userRepository.save(user);
        }
    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);

        var userExists = userRepository.existsById(id);

        if(userExists){
            userRepository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDto createAccountDto) {
        var user = userRepository.findById(UUID.fromString(userId)).
                orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // DTO -> Entity
        var account = new Account(
                UUID.randomUUID(),
                user,
                null,
                createAccountDto.description(),
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                account,
                createAccountDto.street(),
                createAccountDto.number()
        );

        billingAddressRepository.save(billingAddress);
    }
}
