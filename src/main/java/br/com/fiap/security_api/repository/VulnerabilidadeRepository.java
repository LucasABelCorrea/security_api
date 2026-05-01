package br.com.fiap.security_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.security_api.model.Vulnerabilidade;

@Repository
public interface VulnerabilidadeRepository extends JpaRepository<Vulnerabilidade, Long> {

}
