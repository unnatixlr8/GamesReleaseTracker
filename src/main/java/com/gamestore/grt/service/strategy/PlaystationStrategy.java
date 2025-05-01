package com.gamestore.grt.service.strategy;

import com.gamestore.grt.dto.GameDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("playstation")

public class PlaystationStrategy implements StoreStrategy {

    @Override
    public List<GameDto> fetchNewReleases() {
        return null;
    }
}
