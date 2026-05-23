package br.com.fiap.security_api.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class VulnerabilidadeUpdateRequest {
    private String titulo;
    private BigDecimal severidade;
    private BigDecimal versao;
    private Integer qtdAtivosAfetados;
}
