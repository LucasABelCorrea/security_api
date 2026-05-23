package br.com.fiap.security_api.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.fiap.security_api.model.Firewall;

// Deixamos component pois ela só é uma classe que usamos para mapear dados (não chama interface, não fala com banco de dados, etc)
@Component
public class FirewallMapper {

    // O ModelMapper é uma biblioteca para mapeamento de objetos
    private final ModelMapper modelMapper = new ModelMapper();

    public Firewall toModel (FirewallCreateRequest dto) {
        return modelMapper.map(dto, Firewall.class);
    }

    public FirewallResponse toDto (Firewall entity) {
        return modelMapper.map(entity, FirewallResponse.class);
    }

    public Firewall toModel (Long id, FirewallUpdateRequest dto) {
        Firewall firewall = modelMapper.map(dto, Firewall.class);
        firewall.setId(id);
        return firewall;
    }
}
