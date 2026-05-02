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
public class Firewall {
    @Id
    private Long id;

    @Column (length = 30, nullable = false)
    private String nome;

    @Column(length = 30)
    private String cluster;

    @Column(length = 8, nullable = false)
    private BigDecimal numBlades;
}
