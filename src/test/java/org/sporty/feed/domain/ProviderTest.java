package org.sporty.feed.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProviderTest {

    @Test
    void from_alpha_returnsAlpha() {
        assertThat(Provider.from("alpha")).isEqualTo(Provider.ALPHA);
        assertThat(Provider.from("ALPHA")).isEqualTo(Provider.ALPHA);
    }

    @Test
    void from_beta_returnsBeta() {
        assertThat(Provider.from("beta")).isEqualTo(Provider.BETA);
        assertThat(Provider.from("BETA")).isEqualTo(Provider.BETA);
    }

    @Test
    void from_unsupported_throws() {
        assertThatThrownBy(() -> Provider.from("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported provider");
    }
}
