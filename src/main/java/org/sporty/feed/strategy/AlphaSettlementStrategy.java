package org.sporty.feed.strategy;

import org.sporty.feed.domain.MarketOutcome;
import org.sporty.feed.domain.MessageTypeKey;
import org.sporty.feed.domain.Provider;
import org.sporty.feed.domain.StandardBetSettlement;
import org.sporty.feed.domain.StandardMessage;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class AlphaSettlementStrategy implements MessageTransformationStrategy {

    @Override
    public boolean supports(MessageTypeKey key) {
        return Provider.ALPHA.equals(key.provider()) && "settlement".equals(key.rawMessageType());
    }

    @Override
    public StandardMessage transform(JsonNode node) {
        String eventId = node.get("event_id").asString();
        String outcome = node.get("outcome").asString();
        return new StandardBetSettlement(eventId, convert(outcome), Provider.ALPHA);
    }

    private MarketOutcome convert(String value) {
        return switch (value) {
            case "1" -> MarketOutcome.HOME;
            case "X" -> MarketOutcome.DRAW;
            case "2" -> MarketOutcome.AWAY;
            default -> throw new IllegalArgumentException("Invalid outcome");
        };
    }
}

