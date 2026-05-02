package br.com.fiap.security_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Vulnerabilidade {
    @Id
    private Long cve;

    @Column(length = 100, nullable = false)
    private String titulo;

    @Column(length = 10, nullable = false)
    private BigDecimal severidade;

    public Vulnerabilidade(Long cve, String titulo, BigDecimal severidade) {
        this.cve = cve;
        this.titulo = titulo;
        this.severidade = severidade;
    }
}
