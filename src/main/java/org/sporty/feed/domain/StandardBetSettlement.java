package org.sporty.feed.domain;

public class StandardBetSettlement implements StandardMessage {

    private final String eventId;
    private final MarketOutcome result;
    private final Provider provider;

    public StandardBetSettlement(String eventId,
                                 MarketOutcome result, Provider provider) {
        this.eventId = eventId;
        this.result = result;
        this.provider = provider;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BET_SETTLEMENT;
    }

    @Override
    public Provider getProviderId() {
        return this.provider;
    }

    public MarketOutcome getResult() {
        return result;
    }
}

