package br.com.fiap.security_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.security_api.model.Vulnerabilidade;
import br.com.fiap.security_api.repository.VulnerabilidadeRepository;

@Service
public class VulnerabilidadeService {
    @Autowired
    private VulnerabilidadeRepository repository;

    // Insert into e Update
    public Vulnerabilidade createOrUpdate (Vulnerabilidade vulnerabilidade) {
        return repository.save(vulnerabilidade);
    }

    // Select
    public Optional<Vulnerabilidade> findyById (Long id) {
        return repository.findById(id);
    }

    // Select *
    public List<Vulnerabilidade> findAll () {
        return repository.findAll();
    }

    // Delete
    public void deleteById (Long id) {
        repository.deleteById(id);
    }
}
