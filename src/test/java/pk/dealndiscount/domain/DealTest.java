package pk.dealndiscount.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pk.dealndiscount.domain.CardTestSamples.*;
import static pk.dealndiscount.domain.DealTestSamples.*;
import static pk.dealndiscount.domain.StoreTestSamples.*;

import org.junit.jupiter.api.Test;
import pk.dealndiscount.web.rest.TestUtil;

class DealTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deal.class);
        Deal deal1 = getDealSample1();
        Deal deal2 = new Deal();
        assertThat(deal1).isNotEqualTo(deal2);

        deal2.setId(deal1.getId());
        assertThat(deal1).isEqualTo(deal2);

        deal2 = getDealSample2();
        assertThat(deal1).isNotEqualTo(deal2);
    }

    @Test
    void cardTest() {
        Deal deal = getDealRandomSampleGenerator();
        Card cardBack = getCardRandomSampleGenerator();

        deal.setCard(cardBack);
        assertThat(deal.getCard()).isEqualTo(cardBack);

        deal.card(null);
        assertThat(deal.getCard()).isNull();
    }

    @Test
    void storeTest() {
        Deal deal = getDealRandomSampleGenerator();
        Store storeBack = getStoreRandomSampleGenerator();

        deal.setStore(storeBack);
        assertThat(deal.getStore()).isEqualTo(storeBack);

        deal.store(null);
        assertThat(deal.getStore()).isNull();
    }
}
