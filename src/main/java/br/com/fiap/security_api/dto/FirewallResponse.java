package br.com.fiap.security_api.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FirewallResponse {
    private Long id;
    private String nome;
    private String cluster;
    private BigDecimal numBlades;
    private String vendor;
}
