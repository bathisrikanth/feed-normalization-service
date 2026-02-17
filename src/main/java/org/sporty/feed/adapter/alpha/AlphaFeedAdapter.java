package org.sporty.feed.adapter.alpha;

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
public class AlphaFeedAdapter implements FeedAdapter {


    private final ObjectMapper objectMapper;
    private final List<MessageTransformationStrategy> strategies;


    public AlphaFeedAdapter(ObjectMapper objectMapper,
                            List<MessageTransformationStrategy> strategies) {
        this.objectMapper = objectMapper;
        this.strategies = strategies;
    }

    @Override
    public Provider providerName() {
        return Provider.ALPHA;
    }

    @Override
    public StandardMessage transform(String rawPayload) {
        JsonNode node = parse(rawPayload);
        String rawMessageType = node.get("msg_type").asString();
        MessageTypeKey key = MessageTypeKey.of(Provider.ALPHA, rawMessageType);
        return strategies.stream()
                .filter(s -> s.supports(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported Alpha message"))
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
