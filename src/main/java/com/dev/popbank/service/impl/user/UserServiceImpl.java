package com.dev.popbank.service.impl.user;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import com.dev.popbank.mapper.UserMapper;
import com.dev.popbank.model.dto.user.UserPatch;
import com.dev.popbank.model.dto.user.UserPut;
import com.dev.popbank.model.dto.user.UserRequest;
import com.dev.popbank.model.dto.user.UserResponse;
import com.dev.popbank.model.user.UserEntity;
import com.dev.popbank.repository.UserRepository;
import com.dev.popbank.service.UserService;
import com.dev.popbank.service.WalletService;
import org.springframework.data.domain.PageRequest;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final WalletService walletService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, WalletService walletService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.walletService = walletService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void deactivateUserById(UUID id) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        userEntity.setAtivo(false);
    }

    @Override
    @Transactional
    public void activateUserById(UUID id) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        userEntity.setAtivo(true);
    }

    @Override
    @Transactional
    public boolean isActivated(UUID id) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return userEntity.isAtivo();
    }

    @Override
    @Transactional
    public UserEntity createUser(UserRequest userRequest) {
        if (userRepository.findByCpf(userRequest.cpf()) != null) {
            throw new RuntimeException("CPF já cadastrado");
        }

        var userEntity = userMapper.toUserEntity(userRequest);

        userRepository.save(userEntity);

        userEntity.setWallet(walletService.createWallet(userEntity.getId()));
        userEntity.setSenha(passwordEncoder.encode(userRequest.senha()));

        return userEntity;
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }

        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!userEntity.isAtivo()) {
            throw new RuntimeException("Usuário inativo não pode ser deletado");
        }

        userRepository.delete(userEntity);
    }

    @Override
    @Transactional
    public void userPut(UserPut userPut, UUID id) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        userEntity.setNome(userPut.nome());
        userEntity.setNomeDaMae(userPut.nomeDaMae());
        userEntity.setCpf(userPut.cpf());
        userEntity.setDataNascimento(userPut.dataNascimento());

        String encodedPassword = passwordEncoder.encode(userPut.cpf());
        userEntity.setSenha(encodedPassword);
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void userPatch(UserPatch userPatch, UUID id) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (userPatch.nome().isPresent()) {
            userEntity.setNome(userPatch.nome().get());
        }
        if (userPatch.nomeDaMae().isPresent()) {
            userEntity.setNomeDaMae(userPatch.nomeDaMae().get());
        }
        if (userPatch.cpf().isPresent()) {
            userEntity.setCpf(userPatch.cpf().get());
            String encodedPassword = passwordEncoder.encode(userPatch.cpf().get());
            userEntity.setSenha(encodedPassword);
        }
        if (userPatch.dataNascimento().isPresent()) {
            userEntity.setDataNascimento(userPatch.dataNascimento().get());
        }

        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserResponse getUserById(UUID id) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return userMapper.toUserResponse(userEntity);
    }

    @Transactional
    @Override
    public Page<UserResponse> getAllUsers(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        return userMapper.toUserResponsePage(userEntities);
    }
}
