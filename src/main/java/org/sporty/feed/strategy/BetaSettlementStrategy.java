package org.sporty.feed.strategy;

import org.sporty.feed.domain.MarketOutcome;
import org.sporty.feed.domain.MessageTypeKey;
import org.sporty.feed.domain.Provider;
import org.sporty.feed.domain.StandardBetSettlement;
import org.sporty.feed.domain.StandardMessage;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class BetaSettlementStrategy implements MessageTransformationStrategy {

    @Override
    public boolean supports(MessageTypeKey key) {
        return Provider.BETA.equals(key.provider()) && "SETTLEMENT".equals(key.rawMessageType());
    }

    @Override
    public StandardMessage transform(JsonNode node) {
        String eventId = node.get("event_id").asString();
        String result = node.get("result").asString();
        return new StandardBetSettlement(eventId, convert(result), Provider.BETA);
    }

    private MarketOutcome convert(String value) {
        return switch (value.toLowerCase()) {
            case "home" -> MarketOutcome.HOME;
            case "draw" -> MarketOutcome.DRAW;
            case "away" -> MarketOutcome.AWAY;
            default -> throw new IllegalArgumentException("Invalid result: " + value);
        };
    }
}
