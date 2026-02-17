package org.sporty.feed.adapter;

import org.sporty.feed.domain.Provider;
import org.sporty.feed.domain.StandardMessage;

public interface FeedAdapter {
    Provider providerName();

    StandardMessage transform(String rawPayload);
}

