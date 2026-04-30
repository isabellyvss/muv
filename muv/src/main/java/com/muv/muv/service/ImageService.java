package com.muv.muv.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ImageService {

    public String buscarImagem(String termo) {
        try {
            String url = "https://commons.wikimedia.org/w/api.php"
                    + "?action=query"
                    + "&generator=search"
                    + "&gsrsearch=" + URLEncoder.encode(termo, StandardCharsets.UTF_8)
                    + "&gsrlimit=1"
                    + "&prop=imageinfo"
                    + "&iiprop=url"
                    + "&format=json"
                    + "&origin=*";

            RestTemplate rest = new RestTemplate();
            String response = rest.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            JsonNode pages = root.path("query").path("pages");

            for (JsonNode page : pages) {
                String img = page.path("imageinfo").get(0).path("url").asText();
                
                if (!img.endsWith(".svg")) {
                    return img;
                }
            }

            return fallback(termo);

        } catch (Exception e) {
            return fallback(termo);
        }
    }

    private String fallback(String termo) {
        return "https://loremflickr.com/800/600/"
                + termo.replace(" ", "%20")
                + ",city,landmark?random=" + Math.random();
    }

    private String fallback() {
        return "https://images.unsplash.com/photo-1488646953014-85cb44e25828?q=80&w=1000";
    }
}