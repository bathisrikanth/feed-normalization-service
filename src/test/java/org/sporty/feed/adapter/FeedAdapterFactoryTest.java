package org.sporty.feed.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sporty.feed.domain.Provider;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FeedAdapterFactoryTest {

    @Autowired
    private FeedAdapterFactory factory;

    @Test
    void getAdapter_alpha_returnsAlphaAdapter() {
        FeedAdapter adapter = factory.getAdapter(Provider.ALPHA);
        assertThat(adapter).isNotNull();
        assertThat(adapter.providerName()).isEqualTo(Provider.ALPHA);
    }

    @Test
    void getAdapter_beta_returnsBetaAdapter() {
        FeedAdapter adapter = factory.getAdapter(Provider.BETA);
        assertThat(adapter).isNotNull();
        assertThat(adapter.providerName()).isEqualTo(Provider.BETA);
    }

}
