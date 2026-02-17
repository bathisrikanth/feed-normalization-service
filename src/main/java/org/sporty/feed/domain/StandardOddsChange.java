package org.sporty.feed.domain;

import java.util.Map;

public class StandardOddsChange implements StandardMessage {

    private final String eventId;
    private final Map<MarketOutcome, Double> odds;
    private final Provider provider;

    public StandardOddsChange(String eventId,
                              Map<MarketOutcome, Double> odds, Provider provider) {
        this.eventId = eventId;
        this.odds = Map.copyOf(odds);
        this.provider = provider;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ODDS_CHANGE;
    }

    @Override
    public Provider getProviderId() {
        return this.provider;
    }

    @Override
    public String toString() {
        return "StandardOddsChange{" +
                "eventId='" + eventId + '\'' +
                ", odds=" + odds +
                ", provider='" + provider + '\'' +
                '}';
    }

    /** Returns an unmodifiable view of the odds map. */
    public Map<MarketOutcome, Double> getOdds() {
        return odds;
    }


}


