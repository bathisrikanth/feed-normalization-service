package org.sporty.feed.domain;


public enum Provider {
    ALPHA,
    BETA;

    public static Provider from(String value) {
        try {
            return Provider.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unsupported provider: " + value);
        }
    }
}

