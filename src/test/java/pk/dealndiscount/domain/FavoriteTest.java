package pk.dealndiscount.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pk.dealndiscount.domain.FavoriteTestSamples.*;

import org.junit.jupiter.api.Test;
import pk.dealndiscount.web.rest.TestUtil;

class FavoriteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Favorite.class);
        Favorite favorite1 = getFavoriteSample1();
        Favorite favorite2 = new Favorite();
        assertThat(favorite1).isNotEqualTo(favorite2);

        favorite2.setId(favorite1.getId());
        assertThat(favorite1).isEqualTo(favorite2);

        favorite2 = getFavoriteSample2();
        assertThat(favorite1).isNotEqualTo(favorite2);
    }
}
