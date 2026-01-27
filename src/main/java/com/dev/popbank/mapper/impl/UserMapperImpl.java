package com.dev.popbank.mapper.impl;

import com.dev.popbank.mapper.UserMapper;
import com.dev.popbank.model.dto.user.UserRequest;
import com.dev.popbank.model.dto.user.UserResponse;
import com.dev.popbank.model.user.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toUserResponse(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        return new UserResponse(
                userEntity.getId(),
                userEntity.getNome(),
                userEntity.getNomeDaMae(),
                userEntity.getCpf(),
                userEntity.getDataNascimento(),
                userEntity.isAtivo()
        );
    }

    @Override
    public UserEntity toUserEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .nome(userRequest.nome())
                .nomeDaMae(userRequest.nomeDaMae())
                .cpf(userRequest.cpf())
                .dataNascimento(userRequest.dataNascimento())
                .ativo(true)
                .build();
    }

    @Override
    public Page<UserResponse> toUserResponsePage(Page<UserEntity> userEntities) {
        if (userEntities.isEmpty()) {
            throw new NullPointerException("A lista de usuários está vazia");
        }

        return userEntities.map(this::toUserResponse);
    }
}
