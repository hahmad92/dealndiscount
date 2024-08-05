package pk.dealndiscount.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pk.dealndiscount.domain.CityTestSamples.*;

import org.junit.jupiter.api.Test;
import pk.dealndiscount.web.rest.TestUtil;

class CityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(City.class);
        City city1 = getCitySample1();
        City city2 = new City();
        assertThat(city1).isNotEqualTo(city2);

        city2.setId(city1.getId());
        assertThat(city1).isEqualTo(city2);

        city2 = getCitySample2();
        assertThat(city1).isNotEqualTo(city2);
    }
}
