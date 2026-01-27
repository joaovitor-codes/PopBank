package com.dev.popbank.service;

import com.dev.popbank.model.dto.conta.*;
import com.dev.popbank.model.dto.user.UserRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ContaService {
    ContaResponse getContaById(UUID id);
    Page<ContaResponse> getContas(int page);
    void createConta(CreateContaDto createContaDto);
    void deleteContaById(UUID id);
    void contaPut(ContaPutDto contaPutDto, UUID id);
    void contaPatch(ContaPatchDto contaPatchDto, UUID id);
}
