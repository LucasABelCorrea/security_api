package br.com.fiap.security_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FirewallCreateRequest {
    @NotNull
    @Size(min = 2, message = "O tamanho mínimo para o nome é de 8 dígitos")
    private String nome;
    private String cluster;
    private BigDecimal numBlades;
    private String vendor;
}
