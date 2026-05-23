package br.com.fiap.security_api.dto;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
public class VulnerabilidadeResponse {
    private Long cve;
    private String titulo;
    private BigDecimal severidade;
    private BigDecimal versao;
    private Integer qtdAtivosAfetados;
}
