package br.com.fiap.security_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.security_api.model.Firewall;

@Repository
public interface FirewallRepository extends JpaRepository<Firewall, Long>{
    
}
