package org.sporty.feed.service;

import org.sporty.feed.adapter.FeedAdapter;
import org.sporty.feed.adapter.FeedAdapterFactory;
import org.sporty.feed.domain.Provider;
import org.sporty.feed.domain.StandardMessage;
import org.sporty.feed.publisher.MessagePublisher;
import org.springframework.stereotype.Service;

@Service
public class FeedProcessingService {

    private final FeedAdapterFactory factory;
    private final MessagePublisher publisher;

    public FeedProcessingService(FeedAdapterFactory factory,
                                 MessagePublisher publisher) {
        this.factory = factory;
        this.publisher = publisher;
    }

    public void process(Provider provider, String payload) {
        FeedAdapter adapter = factory.getAdapter(provider);
        StandardMessage message = adapter.transform(payload);
        publisher.publish(message);
    }
}
