package br.com.fiap.security_api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class VulnerabilidadeController {

    @GetMapping("/vulnerabilidades") 
    public String vulnerabilidades(){
        return "CVE-2026-24061 encontrada em 78 hosts no ambiente.";
    }
}
