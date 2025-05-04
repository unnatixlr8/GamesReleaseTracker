package com.gamestore.grt.service.strategy;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamestore.grt.dto.GameDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component("nintendo-jp")
public class NintendoJPStrategy implements StoreStrategy{
    private static final String API_URL = "https://search.nintendo.jp/nintendo_soft/search.json";

    @Override
    public List<GameDto> fetchNewReleases() {
        RestTemplate restTemplate = new RestTemplate();


        String encodedFq = URLEncoder.encode("fq[]", StandardCharsets.UTF_8);
        String encodedOpt = URLEncoder.encode("opt_sshow", StandardCharsets.UTF_8);
        String encodedXopt = URLEncoder.encode("xopt_ssitu[]", StandardCharsets.UTF_8);
        String encodedFqValue = URLEncoder.encode("sodate_s:[2025C05 TO *]", StandardCharsets.UTF_8);
        String encodedSort = URLEncoder.encode("sodate asc", StandardCharsets.UTF_8);

        try{
            URI uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                    .queryParam(encodedOpt, "1")
                    .queryParam(encodedXopt, "sales_termination")
                    .queryParam(encodedFq, "")
                    .queryParam(encodedFq,encodedFqValue)
                    .queryParam("limit", "200")
                    .queryParam("page", "1")
                    .queryParam("c", "6348702640203614")
                    .queryParam("sort", encodedSort)
                    .queryParam("opt_sche", "1")
                    .build(true)  // true = encode query params
                    .toUri();

            String jsonStr = restTemplate.getForObject(uri, String.class);
            return parseJsonResponse(jsonStr);
        }
        catch (Exception e){
            throw new RuntimeException("Error in fetching games from Japan eshop");
        }


    }

    private List<GameDto> parseJsonResponse(String jsonStr){

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonStr);
            JsonNode items = root.path("result").path("items");
            List<GameDto> games = new ArrayList<>();
            for(JsonNode game: items){
                if(!game.path("nsuid").isMissingNode() && !game.path("nsuid").isNull()){
                    String name = game.path("title").asText();
                    String storeUrl = "https://store-jp.nintendo.com/item/software/" + "D"+ game.path("nsuid").asText();
                    String dsDate = game.path("dsdate").asText();
                    if (checkReleaseMonth(dsDate)){
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime gameReleaseDate = LocalDateTime.parse(dsDate, inputFormatter);
                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String formattedReleaseDate = gameReleaseDate.format(outputFormatter);
                        games.add(new GameDto(name, storeUrl, formattedReleaseDate));
                    }
                }
            }

            return games;
        }
        catch(Exception e){
            throw new RuntimeException("cannot parse json");
        }
    }

    private boolean checkReleaseMonth(String dsDate){
        if (dsDate == null || dsDate.trim().isEmpty() || dsDate.equalsIgnoreCase("null")) {
            return false;
        }
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime gameReleaseDate = LocalDateTime.parse(dsDate, formatter);
            YearMonth currentYearMonth = YearMonth.now();
            return(gameReleaseDate.getYear() == currentYearMonth.getYear() &&
                    gameReleaseDate.getMonth() == currentYearMonth.getMonth());
        }
        catch(Exception e){
            System.out.println("invalid dsdate" + dsDate);
            return false;
        }

    }
}
