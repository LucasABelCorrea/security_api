package br.com.fiap.security_api.controllers;

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

    //Select
    @GetMapping("/{cve}")
    public ResponseEntity<Vulnerabilidade> findById(@PathVariable Long cve) {
        return repository.findById(cve).map(ResponseEntity :: ok).orElse(ResponseEntity.notFound().build());
    }

    //Update
    @PutMapping("/{cve}")
    public ResponseEntity<Vulnerabilidade> update (@PathVariable Long cve, @RequestBody Vulnerabilidade vulnerabilidade) {
        Optional<Vulnerabilidade> optVulnerabilidade = repository.findById(cve);

        if(optVulnerabilidade.isPresent()) {
            vulnerabilidade.setCve(cve);
            Vulnerabilidade vulnerabilidadeAtualizada = repository.save(vulnerabilidade);
            return ResponseEntity.ok(vulnerabilidadeAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{cve}")
    public ResponseEntity<Void> deleteById (@PathVariable Long cve) {
        repository.deleteById(cve);
        return ResponseEntity.noContent().build();
    }

}
