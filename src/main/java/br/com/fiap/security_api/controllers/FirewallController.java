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

import br.com.fiap.security_api.model.Firewall;
import br.com.fiap.security_api.repository.FirewallRepository;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/${api.version}/firewalls")
public class FirewallController {

    @Autowired
    private FirewallRepository repository;

    //Insert into
    @PostMapping("")
    public ResponseEntity<Firewall> create (@RequestBody Firewall firewall) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(firewall));
    }

    //Select *
    @GetMapping("")
    public ResponseEntity<List<Firewall>> findAll () {
        return ResponseEntity.ok(repository.findAll());
    }

    //Select
    @GetMapping("/{id}")
    public ResponseEntity<Firewall> findById (@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity :: ok).orElse(ResponseEntity.notFound().build());
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<Firewall> update (@PathVariable Long id, Firewall firewall) {
        Optional<Firewall> optFirewall = repository.findById(id);

        if (optFirewall.isPresent()) {
            firewall.setId(id);
            Firewall firewallAtualizado = repository.save(firewall);
            return ResponseEntity.ok(firewallAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}