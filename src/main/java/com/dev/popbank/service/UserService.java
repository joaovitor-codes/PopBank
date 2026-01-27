package com.dev.popbank.service;

import com.dev.popbank.model.dto.user.UserPatch;
import com.dev.popbank.model.dto.user.UserPut;
import com.dev.popbank.model.dto.user.UserRequest;
import com.dev.popbank.model.dto.user.UserResponse;
import com.dev.popbank.model.user.UserEntity;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface UserService {
    void deactivateUserById(UUID id);
    void activateUserById(UUID id);
    boolean isActivated(UUID id);
    UserEntity createUser(UserRequest userRequest);
    void deleteUser(UUID id);
    void userPut(UserPut userPut, UUID id);
    void userPatch(UserPatch userPatch, UUID id);
    Page<UserResponse> getAllUsers(int page);
    UserResponse getUserById(UUID id);
}
