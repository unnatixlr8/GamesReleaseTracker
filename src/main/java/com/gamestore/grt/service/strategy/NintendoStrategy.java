package com.gamestore.grt.service.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import com.gamestore.grt.dto.GameDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("nintendo")
public class NintendoStrategy implements StoreStrategy{

    //start and rows controls pagination
    private static final String API_URL = "https://searching.nintendo-europe.com/en/select";

    @Override
    public List<GameDto> fetchNewReleases() {
        RestTemplate restTemplate = new RestTemplate();

        String fqValue = "type:GAME AND ((dates_released_dts:[NOW-3MONTHS TO NOW])) AND sorting_title:* AND *:*";
        String encodedFq = URLEncoder.encode(fqValue, StandardCharsets.UTF_8);
        String encodedSort = URLEncoder.encode("date_from desc", StandardCharsets.UTF_8);

        try{

            URI uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                    .queryParam("q", "*")
                    .queryParam("fq", encodedFq)
                    .queryParam("sort", encodedSort)
                    .queryParam("start", "0")
                    .queryParam("rows", "48")
                    .queryParam("wt", "json")
                    .build(true)  // true = encode query params
                    .toUri();

            String jsonStr = restTemplate.getForObject(uri, String.class);
            return parseJsonResponse(jsonStr);
        }
        catch(Exception e){
            throw new RuntimeException("failed to fetch games from Nintendo eshop");
        }



    }

    private List<GameDto> parseJsonResponse(String jsonStr){

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonStr);
            JsonNode docs = root.path("response").path("docs");
            List<GameDto> games = new ArrayList<>();
            for(JsonNode game: docs){
                String name = game.path("title").asText();
                String storeUrl = "https://www.nintendo.com" + game.path("url").asText();
                games.add(new GameDto(name, storeUrl));
            }
            return games;
        }
        catch(Exception e){
            throw new RuntimeException("cannot parse json");
        }
    }
}
