package com.dev.popbank.mapper;

import org.springframework.data.domain.Page;
import com.dev.popbank.model.dto.user.UserRequest;
import com.dev.popbank.model.dto.user.UserResponse;
import com.dev.popbank.model.user.UserEntity;

public interface UserMapper {
    UserResponse toUserResponse(UserEntity userEntity);
    UserEntity toUserEntity(UserRequest userRequest);
    Page<UserResponse> toUserResponsePage(Page<UserEntity> userEntities);
}
