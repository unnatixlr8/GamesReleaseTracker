package com.gamestore.grt.service.strategy;

import com.gamestore.grt.dto.GameDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component("steam")
public class SteamStrategy implements StoreStrategy {

    private static final String API_URL = "https://store.steampowered.com/api/featuredcategories";

    @Override
    public List<GameDto> fetchNewReleases(){
        RestTemplate restTemplate = new RestTemplate();
        String jsonStr = restTemplate.getForObject(API_URL, String.class);

        JSONObject json = new JSONObject(jsonStr);
        JSONArray items = json.getJSONObject("new_releases").getJSONArray("items");

        List<GameDto> games = new ArrayList<>();
        for(int i = 0; i < items.length(); i++){
            JSONObject game = items.getJSONObject(i);
            String name = game.getString("name");
            String storeUrl = "https://store.steampowered.com/app/" + game.getInt("id");
            games.add(new GameDto(name, storeUrl));

        }
        return games;
    }
}
