package org.sporty.feed.domain;

/**
 * Identifies a provider-specific message type for strategy selection.
 * Replaces string concatenation (e.g. "ALPHAodds_update") with a type-safe key.
 */
public record MessageTypeKey(Provider provider, String rawMessageType) {
    public static MessageTypeKey of(Provider provider, String rawMessageType) {
        return new MessageTypeKey(provider, rawMessageType);
    }
}
