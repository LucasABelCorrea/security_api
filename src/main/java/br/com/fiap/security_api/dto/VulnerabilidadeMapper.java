package br.com.fiap.security_api.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.fiap.security_api.model.Vulnerabilidade;

@Component
public class VulnerabilidadeMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    // Para criar um DTO para persistir um objeto no BD
    public Vulnerabilidade toModel (VulnerabilidadeCreateRequest dto) {
        return modelMapper.map(dto, Vulnerabilidade.class);
    }

    // Para criar um DTO de resposta (do BD para o Controller)
    public VulnerabilidadeResponse toDto (Vulnerabilidade entity) {
        return modelMapper.map(entity, VulnerabilidadeResponse.class);
    }

    // Para criar um DTO para atualizar um objeto no BD
    public Vulnerabilidade toModel (Long cve, VulnerabilidadeUpdateRequest dto) {
        Vulnerabilidade vulnerabilidade = modelMapper.map(dto, Vulnerabilidade.class);
        vulnerabilidade.setCve(cve);
        return vulnerabilidade;
    }
    
}
