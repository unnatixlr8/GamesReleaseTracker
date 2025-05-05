package com.gamestore.grt.controller;

import com.gamestore.grt.dto.GameDto;
import com.gamestore.grt.factory.StoreStrategyFactory;
import com.gamestore.grt.service.strategy.ExportService;
import com.gamestore.grt.service.strategy.StoreStrategy;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {
    @Autowired
    private StoreStrategyFactory strategyFactory;

    @Autowired
    private HttpSession session;

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
        session.setAttribute("lastFetchedGames", games);
        session.setAttribute("selectedStore", store);
        return "games";

    }

    @GetMapping("/exportToCSV")
    public void exportGamesToCsv(HttpServletResponse response) throws IOException{
        List<GameDto> games = (List<GameDto>) session.getAttribute("lastFetchedGames");
        String store = (String) session.getAttribute("selectedStore");

        if (games == null || games.isEmpty()){
            response.sendError(HttpServletResponse.SC_NO_CONTENT, "No games to export.");
            return;
        }
        if(store == null) store = "unknown";
        String date = LocalDate.now().toString();
        String filename = store.toLowerCase() + "_" + date + ".csv";

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);

        writer.write('\uFEFF');

        //only for eshop japan
        if(store.equals("nintendo-jp")){
            writer.write("Game Name,Store Link,Tentative Release Date\n");
            for(GameDto game: games){
                String title = game.getTitle().replace("\"", "\"\"");
                String storeUrl = game.getStoreUrl().replace("\"", "\"\"");
                String dsDate = game.getDsDate().replace("\"", "\"\"");
                writer.write(String.format("\"%s\",\"%s\",\"%s\"\n",title,storeUrl,dsDate));
            }
        }
        else{
            writer.write("Game Name,Store Link\n");

            for(GameDto game: games){
                String title = game.getTitle().replace("\"", "\"\"");
                String storeUrl = game.getStoreUrl().replace("\"", "\"\"");
                writer.write(String.format("\"%s\",\"%s\"\n",title,storeUrl));
            }
        }

        writer.flush();
        writer.close();
    }

    @GetMapping("/export/all")
    public void exportAllGames(HttpServletResponse response) throws IOException{
        List<String> stores = List.of("steam", "playstation", "nintendo", "nintendo-jp");
        Map<String, List<GameDto>> gamesByStores = new LinkedHashMap<>();

        for(String store: stores){
            StoreStrategy strategy = strategyFactory.getStrategy(store);
            List<GameDto> games = strategy.fetchNewReleases();
            gamesByStores.put(store, games);
        }
        ExportService exportService = new ExportService();
        exportService.exportToExcel(response, gamesByStores);

    }
}
