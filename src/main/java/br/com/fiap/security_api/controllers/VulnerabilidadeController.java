package br.com.fiap.security_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.fiap.security_api.model.Vulnerabilidade;
import br.com.fiap.security_api.repository.VulnerabilidadeRepository;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/${api.version}/vulnerabilidades")
public class VulnerabilidadeController {

    @Autowired
    private VulnerabilidadeRepository repository;

    //Insert into
    @PostMapping("")
    public ResponseEntity<Vulnerabilidade> create (@RequestBody Vulnerabilidade vulnerabilidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(vulnerabilidade));
    }

    //Select *
    @GetMapping("")
    public ResponseEntity<List<Vulnerabilidade>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
