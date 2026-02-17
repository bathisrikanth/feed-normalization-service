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
public class BetaOddsChangeStrategy implements MessageTransformationStrategy {

    @Override
    public boolean supports(MessageTypeKey key) {
        return Provider.BETA.equals(key.provider()) && "ODDS".equals(key.rawMessageType());
    }

    @Override
    public StandardMessage transform(JsonNode node) {

        String eventId = node.get("event_id").asString();

        Map<MarketOutcome, Double> odds = Map.of(
                MarketOutcome.HOME, node.get("odds").get("home").asDouble(),
                MarketOutcome.DRAW, node.get("odds").get("draw").asDouble(),
                MarketOutcome.AWAY, node.get("odds").get("away").asDouble()
        );

        return new StandardOddsChange(eventId, odds, Provider.BETA);
    }
}

