package org.sporty.feed.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StandardBetSettlementTest {

    @Test
    void getEventId_getMessageType_getProviderId_getResult_returnsCorrectValues() {
        StandardBetSettlement msg = new StandardBetSettlement("ev1", MarketOutcome.HOME, Provider.BETA);
        assertThat(msg.getEventId()).isEqualTo("ev1");
        assertThat(msg.getMessageType()).isEqualTo(MessageType.BET_SETTLEMENT);
        assertThat(msg.getProviderId()).isEqualTo(Provider.BETA);
        assertThat(msg.getResult()).isEqualTo(MarketOutcome.HOME);
    }
}
