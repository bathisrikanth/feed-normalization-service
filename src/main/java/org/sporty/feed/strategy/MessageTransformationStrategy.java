package org.sporty.feed.strategy;

import org.sporty.feed.domain.MessageTypeKey;
import org.sporty.feed.domain.StandardMessage;
import tools.jackson.databind.JsonNode;

public interface MessageTransformationStrategy {
    boolean supports(MessageTypeKey key);
    StandardMessage transform(JsonNode payload);
}

