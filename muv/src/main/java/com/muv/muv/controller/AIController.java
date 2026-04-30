package com.muv.muv.controller;

import com.muv.muv.service.AIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/destinos")
    public String destinos(@RequestParam String query) {
        return aiService.gerarDestinos(query);
    }

    @GetMapping("/roteiro")
    public String roteiro(
            @RequestParam String destino,
            @RequestParam(required = false) String preferencias
    ) {
        return aiService.gerarRoteiro(destino, preferencias);
    }
}