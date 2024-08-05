package pk.dealndiscount.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pk.dealndiscount.web.rest.TestUtil;

class DealDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DealDTO.class);
        DealDTO dealDTO1 = new DealDTO();
        dealDTO1.setId(1L);
        DealDTO dealDTO2 = new DealDTO();
        assertThat(dealDTO1).isNotEqualTo(dealDTO2);
        dealDTO2.setId(dealDTO1.getId());
        assertThat(dealDTO1).isEqualTo(dealDTO2);
        dealDTO2.setId(2L);
        assertThat(dealDTO1).isNotEqualTo(dealDTO2);
        dealDTO1.setId(null);
        assertThat(dealDTO1).isNotEqualTo(dealDTO2);
    }
}
