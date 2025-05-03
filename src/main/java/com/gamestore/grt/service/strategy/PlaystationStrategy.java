package com.gamestore.grt.service.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamestore.grt.dto.GameDto;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("playstation")

public class PlaystationStrategy implements StoreStrategy {
    private static final String API_URL = "https://web.np.playstation.com/api/graphql/v1//op";
    private static final String CATEGORY_ID = "e1699f77-77e1-43ca-a296-26d08abacb0f";
    private static final String SHA256HASH = "be843d8d063502a54309ccfbedbcefaad4de7f923f8952a6f098ff388df0f25a";

    @Override
    public List<GameDto> fetchNewReleases() {
        RestTemplate restTemplate = new RestTemplate();

        String variables = String.format(
                "{\"id\":\"%s\",\"pageArgs\":{\"size\":48,\"offset\":0},\"sortBy\":{\"name\":\"conceptReleaseDate\",\"isAscending\":false},\"filterBy\":[],\"facetOptions\":[]}",
                CATEGORY_ID
        );

        String extensions = String.format(
                "{\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"%s\"}}",
                SHA256HASH
        );

        URI uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("operationName", "categoryGridRetrieve")
                .queryParam("variables", variables)
                .queryParam("extensions", extensions)
                .build()
                .toUri();


        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-apollo-operation-name", "categoryGridRetrieve");
        headers.set("apollo-require-preflight", "true");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        headers.set("Origin", "https://www.playstation.com");
        headers.set("Referer", "https://www.playstation.com/");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return parseJsonResponse(response.getBody());
    }

    private List<GameDto> parseJsonResponse(String jsonStr){
        List<GameDto> games = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            JsonNode root = objectMapper.readTree(jsonStr);
            JsonNode concepts = root.path("data").path("categoryGridRetrieve").path("concepts");

            for(JsonNode gameNode: concepts){
                String name = gameNode.path("name").asText();
                String productId = gameNode.path("id").asText();
                String storeUrl = "https://store.playstation.com/en-us/concept/" + productId;
                games.add(new GameDto(name, storeUrl));
            }

        }
        catch(Exception e){
            throw new RuntimeException("Failed to parse API response", e);
        }
        return games;
    }
}
