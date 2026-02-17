package org.sporty.feed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.sporty.feed.domain.MessageType;
import org.sporty.feed.domain.StandardMessage;
import org.sporty.feed.publisher.MessagePublisher;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests: full flow from HTTP endpoint through adapter, strategy, and publisher.
 * Uses exact message formats from the assignment specification.
 */
@SpringBootTest
@AutoConfigureMockMvc
class FlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private MessagePublisher publisher;

    @Test
    void providerAlpha_oddsUpdate_fullFlow_publisherReceivesOddsChange() throws Exception {
        String payload = """
                {"msg_type": "odds_update", "event_id": "ev123", "values": {"1": 2.0, "X": 3.1, "2": 3.8}}
                """;
        mockMvc.perform(post("/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
        verify(publisher).publish(argThat((StandardMessage m) ->
                m.getMessageType() == MessageType.ODDS_CHANGE && "ev123".equals(m.getEventId())));
    }

    @Test
    void providerAlpha_settlement_fullFlow_publisherReceivesBetSettlement() throws Exception {
        String payload = """
                {"msg_type": "settlement", "event_id": "ev123", "outcome": "1"}
                """;
        mockMvc.perform(post("/provider-alpha/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
        verify(publisher).publish(argThat((StandardMessage m) ->
                m.getMessageType() == MessageType.BET_SETTLEMENT && "ev123".equals(m.getEventId())));
    }

    @Test
    void providerBeta_odds_fullFlow_publisherReceivesOddsChange() throws Exception {
        String payload = """
                {"type": "ODDS", "event_id": "ev456", "odds": {"home": 1.95, "draw": 3.2, "away": 4.0}}
                """;
        mockMvc.perform(post("/provider-beta/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
        verify(publisher).publish(argThat((StandardMessage m) ->
                m.getMessageType() == MessageType.ODDS_CHANGE && "ev456".equals(m.getEventId())));
    }

    @Test
    void providerBeta_settlement_fullFlow_publisherReceivesBetSettlement() throws Exception {
        String payload = """
                {"type": "SETTLEMENT", "event_id": "ev456", "result": "away"}
                """;
        mockMvc.perform(post("/provider-beta/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
        verify(publisher).publish(argThat((StandardMessage m) ->
                m.getMessageType() == MessageType.BET_SETTLEMENT && "ev456".equals(m.getEventId())));
    }
}
