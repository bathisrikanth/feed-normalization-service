package org.sporty.feed.strategy;

import org.sporty.feed.domain.MarketOutcome;
import org.sporty.feed.domain.MessageTypeKey;
import org.sporty.feed.domain.Provider;
import org.sporty.feed.domain.StandardMessage;
import org.sporty.feed.domain.StandardOddsChange;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.Map;

@Component
public class AlphaOddsChangeStrategy implements MessageTransformationStrategy {

    @Override
    public boolean supports(MessageTypeKey key) {
        return Provider.ALPHA.equals(key.provider()) && "odds_update".equals(key.rawMessageType());
    }

    @Override
    public StandardMessage transform(JsonNode node) {

        String eventId = node.get("event_id").asString();

        Map<MarketOutcome, Double> odds = Map.of(
                MarketOutcome.HOME, node.get("values").get("1").asDouble(),
                MarketOutcome.DRAW, node.get("values").get("X").asDouble(),
                MarketOutcome.AWAY, node.get("values").get("2").asDouble()
        );

        return new StandardOddsChange(eventId, odds, Provider.ALPHA);
    }
}

