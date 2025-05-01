package com.gamestore.grt.factory;

import com.gamestore.grt.service.strategy.StoreStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class StoreStrategyFactory {
    private final Map<String, StoreStrategy> strategies;
    @Autowired
    public StoreStrategyFactory(Map<String, StoreStrategy> strategies){
        this.strategies = strategies;
    }
    public StoreStrategy getStrategy(String store){
        return strategies.get(store.toLowerCase());
    }
}
