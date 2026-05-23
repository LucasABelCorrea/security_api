package br.com.fiap.security_api.dto;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VulnerabilidadeCreateRequest {
    private String titulo;
    private BigDecimal severidade;
    private BigDecimal versao;
    private Integer qtdAtivosAfetados;
}
