package com.dev.popbank.controller;

import com.dev.popbank.model.dto.user.UserPatch;
import com.dev.popbank.model.dto.user.UserPut;
import com.dev.popbank.model.dto.user.UserRequest;
import com.dev.popbank.model.dto.user.UserResponse;
import com.dev.popbank.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam("from") int page) {
        return ResponseEntity.ok(userService.getAllUsers(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ativa")
    public ResponseEntity<Void> activateUser(@PathVariable UUID id) {
        userService.activateUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/desativa")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        userService.deactivateUserById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody UserPut userPut) {
        userService.userPut(userPut, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody UserPatch userPatch) {
        userService.userPatch(userPatch, id);
        return ResponseEntity.noContent().build();
    }
}
