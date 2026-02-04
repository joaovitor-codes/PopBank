package com.dev.popbank.service.impl.conta;

import com.dev.popbank.exception.AccountNotFoundException;
import com.dev.popbank.exception.EmailAlreadyExistsException;
import com.dev.popbank.exception.UserNotFoundException;
import com.dev.popbank.mapper.ContaMapper;
import com.dev.popbank.model.auth.ContaEntity;
import com.dev.popbank.model.dto.conta.*;
import com.dev.popbank.model.dto.user.UserRequest;
import com.dev.popbank.repository.ContaRepository;
import com.dev.popbank.repository.UserRepository;
import com.dev.popbank.service.ContaService;
import com.dev.popbank.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ContaServiceImpl implements ContaService {
    private final ContaRepository contaRepository;
    private final UserService userService;
    private final ContaMapper contaMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ContaServiceImpl(ContaRepository contaRepository, UserService userService, ContaMapper contaMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.contaRepository = contaRepository;
        this.userService = userService;
        this.contaMapper = contaMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public ContaResponse getContaById(UUID id) {
        if (id == null){
            throw new IllegalArgumentException("ID da conta não pode ser nulo");
        }

        var contaEntity = contaRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada"));

        return contaMapper.toContaResponse(contaEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContaResponse> getContas(int page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<ContaEntity> contaEntitiesPage = contaRepository.findAll(pageable);
        return contaMapper.toContaResponsePage(contaEntitiesPage);
    }

    @Override
    @Transactional
    public void createConta(CreateContaDto createContaDto) {
        ContaRequest contaRequest = new ContaRequest(
                createContaDto.login(),
                createContaDto.password()
        );

        UserRequest userRequest = new UserRequest(
                createContaDto.nome(),
                createContaDto.nomeDaMae(),
                createContaDto.cpf(),
                createContaDto.dataNascimento(),
                createContaDto.password()
        );

        if (contaRepository.existsByLogin(contaRequest.login())){
            throw new EmailAlreadyExistsException("Email já está em uso");
        }

        var user = userService.createUser(userRequest);

        var contaEntity = contaMapper.toContaEntity(contaRequest);

        String encodedPassword = passwordEncoder.encode(contaRequest.password());
        contaEntity.setSenha(encodedPassword);

        user.setAtivo(true);
        contaEntity.setUsuario(user);
        user.setConta(contaEntity);

        userRepository.save(user);
        contaRepository.save(contaEntity);
    }

    @Override
    @Transactional
    public void deleteContaById(UUID id){
        if (id == null){
            throw new IllegalArgumentException("ID da conta não pode ser nulo");
        }

        if (contaRepository.existsById(id)){
            contaRepository.deleteById(id);
        } else {
            throw new AccountNotFoundException("Conta não encontrada");
        }
    }

    @Override
    @Transactional
    public void contaPut(ContaPutDto contaPutDto, UUID id) {
        if (id == null){
            throw new IllegalArgumentException("ID da conta não pode ser nulo");
        }

        var contaEntity = contaRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada"));

        String encodedPassword = passwordEncoder.encode(contaPutDto.senha());
        contaEntity.setLogin(contaPutDto.login());
        contaEntity.setSenha(encodedPassword);
        contaRepository.save(contaEntity);
    }

    @Override
    @Transactional
    public void contaPatch(ContaPatchDto contaPatchDto, UUID id) {
        if (id == null){
            throw new IllegalArgumentException("ID da conta não pode ser nulo");
        }

        var contaEntity = contaRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada"));

        if(contaEntity.getUsuario() == null){
            throw new UserNotFoundException("Usuário não encontrado");
        }

        if (contaPatchDto.login().isPresent()){
            contaEntity.setLogin(contaPatchDto.login().get());
        }

        if (contaPatchDto.senha().isPresent()){
            String encodedPassword = passwordEncoder.encode(contaPatchDto.senha().get());
            contaEntity.setSenha(encodedPassword);
        }

        contaRepository.save(contaEntity);
    }
}
