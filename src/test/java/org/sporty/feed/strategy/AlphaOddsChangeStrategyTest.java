package org.sporty.feed.strategy;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sporty.feed.domain.MarketOutcome;
import org.sporty.feed.domain.MessageTypeKey;
import org.sporty.feed.domain.Provider;
import org.sporty.feed.domain.StandardMessage;
import org.sporty.feed.domain.StandardOddsChange;

import static org.assertj.core.api.Assertions.assertThat;

class AlphaOddsChangeStrategyTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private AlphaOddsChangeStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new AlphaOddsChangeStrategy();
    }

    @Test
    void supports_alphaOddsUpdate_returnsTrue() {
        assertThat(strategy.supports(MessageTypeKey.of(Provider.ALPHA, "odds_update"))).isTrue();
    }

    @Test
    void supports_other_returnsFalse() {
        assertThat(strategy.supports(MessageTypeKey.of(Provider.ALPHA, "settlement"))).isFalse();
        assertThat(strategy.supports(MessageTypeKey.of(Provider.BETA, "ODDS"))).isFalse();
    }

    @Test
    void transform_producesStandardOddsChange() throws Exception {
        String json = "{\"event_id\": \"ev123\", \"values\": {\"1\": 2.0, \"X\": 3.1, \"2\": 3.8}}";
        JsonNode node = objectMapper.readTree(json);

        StandardMessage result = strategy.transform(node);

        assertThat(result).isInstanceOf(StandardOddsChange.class);
        StandardOddsChange odds = (StandardOddsChange) result;
        assertThat(odds.getEventId()).isEqualTo("ev123");
        assertThat(odds.getProviderId()).isEqualTo(Provider.ALPHA);
        assertThat(odds.getOdds()).containsEntry(MarketOutcome.HOME, 2.0);
        assertThat(odds.getOdds()).containsEntry(MarketOutcome.DRAW, 3.1);
        assertThat(odds.getOdds()).containsEntry(MarketOutcome.AWAY, 3.8);
    }
}
