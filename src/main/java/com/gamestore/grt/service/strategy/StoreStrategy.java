package com.gamestore.grt.service.strategy;

import com.gamestore.grt.dto.GameDto;

import java.util.List;

public interface StoreStrategy {
    List<GameDto> fetchNewReleases();
}
