package br.com.fiap.security_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "vulnerabilidades")
public class Vulnerabilidade {
    @Id
    private Long cve;

    @Column(length = 100, nullable = false)
    private String titulo;

    @Column(length = 10, nullable = false)
    private BigDecimal severidade;

    @Column(length = 10, nullable = false)
    private BigDecimal versao;

    @Column(nullable = false)
    private Integer qtdAtivosAfetados;
}
