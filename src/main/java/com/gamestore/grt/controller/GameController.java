package com.gamestore.grt.controller;

import com.gamestore.grt.dto.GameDto;
import com.gamestore.grt.factory.StoreStrategyFactory;
import com.gamestore.grt.service.strategy.StoreStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class GameController {
    @Autowired
    private StoreStrategyFactory strategyFactory;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/games")
    public String showGames(@RequestParam String store, Model model){
        StoreStrategy strategy = strategyFactory.getStrategy(store);
        if(strategy == null){
            model.addAttribute("store", "Unsupported store");
            model.addAttribute("games", List.of());
            return "games";

        }

        List<GameDto> games = strategy.fetchNewReleases();
        model.addAttribute("games", games);
        model.addAttribute("store", store);
        return "games";

    }
}
