package br.com.fiap.security_api.controller;

import java.util.List;

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

import br.com.fiap.security_api.dto.FirewallCreateRequest;
import br.com.fiap.security_api.dto.FirewallMapper;
import br.com.fiap.security_api.dto.FirewallResponse;
import br.com.fiap.security_api.dto.FirewallUpdateRequest;
import br.com.fiap.security_api.model.Firewall;
import br.com.fiap.security_api.service.FirewallService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/${api.version}/firewalls")
public class FirewallController {

    @Autowired
    private FirewallService service;

    @Autowired
    private FirewallMapper firewallMapper;

    //Insert into
    @PostMapping("")
    public ResponseEntity<FirewallResponse> create (@Valid @RequestBody FirewallCreateRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(firewallMapper.toDto(service.createOrUpdate(firewallMapper.toModel(dtoRequest))));
    }

    //Select *
    @GetMapping("")
    public ResponseEntity<List<FirewallResponse>> findAll () {
        return ResponseEntity.ok(service.findAll().stream().map(firewall -> firewallMapper.toDto(firewall)).toList());
    }

    //Select
    @GetMapping("/{id}")
    public ResponseEntity<FirewallResponse> findById (@PathVariable Long id) {
        return service.findById(id).map(firewall -> firewallMapper.toDto(firewall)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<FirewallResponse> update (@PathVariable Long id, @Valid @RequestBody FirewallUpdateRequest dtoRequest) {

        if (service.findById(id).isPresent()) {
            Firewall firewallAtualizado = firewallMapper.toModel(id, dtoRequest);
            firewallAtualizado.setId(id);
            return ResponseEntity.ok(firewallMapper.toDto(service.createOrUpdate(firewallAtualizado)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}