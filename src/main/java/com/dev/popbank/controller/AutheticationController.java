package com.dev.popbank.controller;

import com.dev.popbank.config.security.TokenService;
import com.dev.popbank.model.auth.ContaEntity;
import com.dev.popbank.model.auth.ContasRole;
import com.dev.popbank.model.dto.conta.AuthenticationDto;
import com.dev.popbank.model.dto.conta.CreateContaDto;
import com.dev.popbank.model.dto.conta.LoginDto;
import com.dev.popbank.model.dto.conta.RegisterAdmDto;
import com.dev.popbank.model.dto.conta.RegisterDto;
import com.dev.popbank.repository.ContaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutheticationController {
    private final AuthenticationManager authenticationManager;
    private final ContaRepository contaRepository;
    private final TokenService tokenService;
    private final com.dev.popbank.service.ContaService contaService;

    public AutheticationController(AuthenticationManager authenticationManager, ContaRepository contaRepository, TokenService tokenService, com.dev.popbank.service.ContaService contaService) {
        this.authenticationManager = authenticationManager;
        this.contaRepository = contaRepository;
        this.tokenService = tokenService;
        this.contaService = contaService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody @Valid AuthenticationDto data){
        var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(userNamePassword);

        var token = tokenService.generateToken((ContaEntity) auth.getPrincipal());

        return ResponseEntity.ok(new LoginDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid CreateContaDto data){
        contaService.createConta(data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register-admin")
    public ResponseEntity<Void> registerAdmin(@RequestBody @Valid RegisterAdmDto data){
        if (this.contaRepository.findByLogin(data.login()) != null){
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        ContaEntity conta = new ContaEntity(data.login(), encryptedPassword, data.role());
        this.contaRepository.save(conta);

        return ResponseEntity.ok().build();
    }
}
