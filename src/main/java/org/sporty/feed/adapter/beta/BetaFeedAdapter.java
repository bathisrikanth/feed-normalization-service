package org.sporty.feed.adapter.beta;

import org.sporty.feed.adapter.FeedAdapter;
import org.sporty.feed.exception.InvalidPayloadException;
import org.sporty.feed.domain.MessageTypeKey;
import org.sporty.feed.domain.Provider;
import org.sporty.feed.domain.StandardMessage;
import org.sporty.feed.strategy.MessageTransformationStrategy;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class BetaFeedAdapter implements FeedAdapter {


    private final ObjectMapper objectMapper;
    private final List<MessageTransformationStrategy> strategies;

    @Override
    public Provider providerName() {
        return Provider.BETA;
    }

    public BetaFeedAdapter(ObjectMapper objectMapper,
                            List<MessageTransformationStrategy> strategies) {
        this.objectMapper = objectMapper;
        this.strategies = strategies;
    }

    @Override
    public StandardMessage transform(String rawPayload) {
        JsonNode node = parse(rawPayload);
        String rawMessageType = node.get("type").asString();
        MessageTypeKey key = MessageTypeKey.of(Provider.BETA, rawMessageType);
        return strategies.stream()
                .filter(s -> s.supports(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported Beta message"))
                .transform(node);
    }

    private JsonNode parse(String payload) {
        try {
            return objectMapper.readTree(payload);
        } catch (Exception e) {
            throw new InvalidPayloadException("Invalid JSON", e);
        }
    }
}
