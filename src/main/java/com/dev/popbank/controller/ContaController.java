package com.dev.popbank.controller;

import com.dev.popbank.model.dto.conta.*;
import com.dev.popbank.service.ContaService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/contas")
public class ContaController {
    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping()
    public ResponseEntity<Page<ContaResponse>> getAllContas(@RequestParam("from") int page) {
        return ResponseEntity.ok(contaService.getContas(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> getContaById(@PathVariable UUID id) {
        return ResponseEntity.ok(contaService.getContaById(id));
    }

    @PostMapping
    public ResponseEntity<Void> criarConta(@RequestBody CreateContaDto createContaDto) {
        contaService.createConta(createContaDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConta(@PathVariable UUID id) {
        contaService.deleteContaById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateConta(@PathVariable UUID id, @RequestBody ContaPutDto contaPutDto) {
        contaService.contaPut(contaPutDto, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchConta(@PathVariable UUID id, @RequestBody ContaPatchDto contaPatchDto) {
        contaService.contaPatch(contaPatchDto, id);
        return ResponseEntity.noContent().build();
    }
}
