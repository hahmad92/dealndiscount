package pk.dealndiscount.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pk.dealndiscount.domain.CityTestSamples.*;
import static pk.dealndiscount.domain.StoreTestSamples.*;

import org.junit.jupiter.api.Test;
import pk.dealndiscount.web.rest.TestUtil;

class StoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Store.class);
        Store store1 = getStoreSample1();
        Store store2 = new Store();
        assertThat(store1).isNotEqualTo(store2);

        store2.setId(store1.getId());
        assertThat(store1).isEqualTo(store2);

        store2 = getStoreSample2();
        assertThat(store1).isNotEqualTo(store2);
    }

    @Test
    void cityTest() {
        Store store = getStoreRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        store.setCity(cityBack);
        assertThat(store.getCity()).isEqualTo(cityBack);

        store.city(null);
        assertThat(store.getCity()).isNull();
    }
}
