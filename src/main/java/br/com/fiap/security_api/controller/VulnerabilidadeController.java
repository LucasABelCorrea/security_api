package br.com.fiap.security_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.security_api.dto.VulnerabilidadeCreateRequest;
import br.com.fiap.security_api.dto.VulnerabilidadeMapper;
import br.com.fiap.security_api.dto.VulnerabilidadeResponse;
import br.com.fiap.security_api.dto.VulnerabilidadeUpdateRequest;
import br.com.fiap.security_api.model.Vulnerabilidade;
import br.com.fiap.security_api.repository.VulnerabilidadeRepository;
import br.com.fiap.security_api.service.VulnerabilidadeService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/${api.version}/vulnerabilidades")
public class VulnerabilidadeController {

    @Autowired
    private VulnerabilidadeService service;

    @Autowired
    private VulnerabilidadeMapper vulnerabilidadeMapper;

    //Insert into
    @PostMapping("")
    public ResponseEntity<VulnerabilidadeResponse> create (@Valid @RequestBody VulnerabilidadeCreateRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vulnerabilidadeMapper.toDto(service.createOrUpdate(vulnerabilidadeMapper.toModel(dtoRequest))));
    }

    //Select *
    @GetMapping("")
    public ResponseEntity<List<VulnerabilidadeResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(vulnerabilidade -> vulnerabilidadeMapper.toDto(vulnerabilidade)).toList());
    }

    //Select
    @GetMapping("/{cve}")
    public ResponseEntity<VulnerabilidadeResponse> findById(@PathVariable Long cve) {
        return service.findyById(cve).map(vulnerabilidade -> vulnerabilidadeMapper.toDto(vulnerabilidade)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Update
    @PutMapping("/{cve}")
    public ResponseEntity<VulnerabilidadeResponse> update (@PathVariable Long cve, @Valid @RequestBody VulnerabilidadeUpdateRequest dtoRequest) {

        if(service.findyById(cve).isPresent()) {
            Vulnerabilidade vulnerabilidadeAtualizada = vulnerabilidadeMapper.toModel(cve, dtoRequest);
            vulnerabilidadeAtualizada.setCve(cve);
            return ResponseEntity.ok(vulnerabilidadeMapper.toDto(service.createOrUpdate(vulnerabilidadeAtualizada)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{cve}")
    public ResponseEntity<Void> deleteById (@PathVariable Long cve) {
        if (service.findyById(cve).isPresent()) {
            service.deleteById(cve);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
