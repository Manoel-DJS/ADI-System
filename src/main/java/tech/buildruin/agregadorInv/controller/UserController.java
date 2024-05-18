package tech.buildruin.agregadorInv.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.buildruin.agregadorInv.controller.dto.AccountResponseDto;
import tech.buildruin.agregadorInv.controller.dto.CreateAccountDto;
import tech.buildruin.agregadorInv.controller.dto.CreateUserDto;
import tech.buildruin.agregadorInv.controller.dto.UpdateUserDto;
import tech.buildruin.agregadorInv.entity.Account;
import tech.buildruin.agregadorInv.entity.User;
import tech.buildruin.agregadorInv.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto){
        var userId = userService.createUser(createUserDto);

        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    //Consulta de usuários pelo  ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId){
        var user = userService.getUserById(userId);
        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else{
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(){
        var users = userService.ListUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable("userId") String userId,
                                               @RequestBody UpdateUserDto updateUserDto){
        userService.uptadeUserById(userId, updateUserDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId ){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<Void> getAccountById(@PathVariable("userId") String userId,
                                           @RequestBody CreateAccountDto createAccountDto){
        userService.createAccount(userId, createAccountDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDto>> listAccounts(@PathVariable("userId") String userId){
        var accounts = userService.listAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

}
