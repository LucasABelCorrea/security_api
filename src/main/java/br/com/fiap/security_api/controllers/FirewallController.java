package br.com.fiap.security_api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firewall")
public class FirewallController {

    @GetMapping
    public String status(){
        return "Todos os firewalls do ambiente estão ativos";
    }

    @GetMapping("/licencas")
    public String licencas() {
        return "As licenças de IPS e Web Filtering estão habilitadas";
    }
}