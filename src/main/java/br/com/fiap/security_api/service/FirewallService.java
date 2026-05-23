package br.com.fiap.security_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.security_api.model.Firewall;
import br.com.fiap.security_api.repository.FirewallRepository;

@Service
public class FirewallService {
    
    @Autowired
    private FirewallRepository repository;

    // Insert ou Update
    public Firewall createOrUpdate (Firewall firewall) {
        return repository.save(firewall);
    }

    // Select *
    public List<Firewall> findAll () {
        return repository.findAll();
    }

    // Select
    public Optional<Firewall> findById (Long id) {
        return repository.findById(id);
    }

    // Delete
    public void deleteById (Long id) {
        repository.deleteById(id);
    }
}
