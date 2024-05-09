package tech.buildruin.agregadorInv.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.buildruin.agregadorInv.entity.User;
import tech.buildruin.agregadorInv.service.UserService;

import java.net.URI;

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

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId){
        var user = userService.getUserById(userId);
        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else{
            return ResponseEntity.notFound().build();
        }

    }
}
