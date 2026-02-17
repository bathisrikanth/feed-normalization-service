package org.sporty.feed.adapter;

import org.sporty.feed.domain.Provider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class FeedAdapterFactory {
    private final Map<Provider, FeedAdapter> adapters;

    public FeedAdapterFactory(List<FeedAdapter> adaptersList){
        this.adapters = adaptersList.stream()
                .collect(Collectors.toMap(FeedAdapter::providerName, Function.identity()));
    }

    public FeedAdapter getAdapter(Provider provider){
        FeedAdapter adapter = adapters.get(provider);
        if (adapter == null) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
        return adapter;
    }
}
