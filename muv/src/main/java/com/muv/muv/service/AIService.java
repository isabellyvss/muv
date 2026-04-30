package com.muv.muv.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

@Service
public class AIService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    public String gerarDestinos(String query) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            // 🔥 PROMPT CORRIGIDO
            String prompt = """
AJA COMO UM AGENTE DE VIAGENS ESPECIALISTA.

Considere a solicitação abaixo e gere um JSON com 8 destinos compatíveis.

SOLICITAÇÃO:
%s

REGRAS:
1. Nome no formato: Cidade, País
2. Apenas destinos dentro da região solicitada
3. Preços REALISTAS em reais (R$) saindo do Brasil:
   - América do Sul: R$ 800 - R$ 2500
   - América do Norte: R$ 2500 - R$ 6000
   - Europa: R$ 4000 - R$ 12000
   - Ásia: R$ 5000 - R$ 15000
4. Categoria: praia, cidade, historico, natureza ou templo

Formato:
{
  "destinos": [
    {
      "nome": "",
      "preco": "R$ 5000 - R$ 8000",
      "epoca": "",
      "motivo": "",
      "categoria": ""
    }
  ]
}
""".formatted(query);

            // Corpo da requisição
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("model", "llama-3.1-8b-instant");
            bodyMap.put("messages", List.of(Map.of("role", "user", "content", prompt)));
            bodyMap.put("response_format", Map.of("type", "json_object"));

            String body = mapper.writeValueAsString(bodyMap);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            // 🔥 extrai resposta da IA
            JsonNode root = mapper.readTree(response.getBody());
            String jsonContent = root.path("choices").get(0).path("message").path("content").asText();

            // 🔥 garante JSON válido
            JsonNode json = mapper.readTree(jsonContent);
            ArrayNode destinosArray = (ArrayNode) json.path("destinos");

            // 🔥 fallback de segurança
            if (destinosArray == null || destinosArray.isEmpty()) {
                return "{\"destinos\": []}";
            }

            return mapper.writeValueAsString(json);

        } catch (Exception e) {
            System.err.println("Erro Muv: " + e.getMessage());
            e.printStackTrace();
            return "{\"destinos\": []}";
        }
    }
    public String gerarRoteiro(String destino) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String prompt = "Crie um roteiro de viagem de 3 dias para " + destino +
                    ". Retorne em JSON no formato: " +
                    "{\"dias\":[{\"dia\":\"Dia 1\",\"atividades\":[\"...\"]}]}";

            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.1-8b-instant");
            body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
            body.put("response_format", Map.of("type", "json_object"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(body), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            var root = mapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            return "{\"dias\":[]}";
        }
    }
    public String gerarRoteiro(String destino, String preferencias) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String prompt = "Crie um roteiro de 3 dias para " + destino;

            if (preferencias != null && !preferencias.isEmpty()) {
                prompt += ", focado em: " + preferencias;
            }

            prompt += ". Retorne em JSON no formato: {\"dias\": [{\"titulo\": \"\", \"atividades\": []}]}";

            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.1-8b-instant");
            body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
            body.put("response_format", Map.of("type", "json_object"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(body), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            var root = mapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"dias\":[]}";
        }
    }
}