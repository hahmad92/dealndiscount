package pk.dealndiscount.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pk.dealndiscount.domain.CityTestSamples.*;
import static pk.dealndiscount.domain.ExtendedUserTestSamples.*;

import org.junit.jupiter.api.Test;
import pk.dealndiscount.web.rest.TestUtil;

class ExtendedUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtendedUser.class);
        ExtendedUser extendedUser1 = getExtendedUserSample1();
        ExtendedUser extendedUser2 = new ExtendedUser();
        assertThat(extendedUser1).isNotEqualTo(extendedUser2);

        extendedUser2.setId(extendedUser1.getId());
        assertThat(extendedUser1).isEqualTo(extendedUser2);

        extendedUser2 = getExtendedUserSample2();
        assertThat(extendedUser1).isNotEqualTo(extendedUser2);
    }

    @Test
    void cityTest() {
        ExtendedUser extendedUser = getExtendedUserRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        extendedUser.setCity(cityBack);
        assertThat(extendedUser.getCity()).isEqualTo(cityBack);

        extendedUser.city(null);
        assertThat(extendedUser.getCity()).isNull();
    }
}
