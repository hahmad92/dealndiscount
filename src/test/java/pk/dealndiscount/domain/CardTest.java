package pk.dealndiscount.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pk.dealndiscount.domain.BankTestSamples.*;
import static pk.dealndiscount.domain.CardTestSamples.*;

import org.junit.jupiter.api.Test;
import pk.dealndiscount.web.rest.TestUtil;

class CardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
        Card card1 = getCardSample1();
        Card card2 = new Card();
        assertThat(card1).isNotEqualTo(card2);

        card2.setId(card1.getId());
        assertThat(card1).isEqualTo(card2);

        card2 = getCardSample2();
        assertThat(card1).isNotEqualTo(card2);
    }

    @Test
    void bankTest() {
        Card card = getCardRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        card.setBank(bankBack);
        assertThat(card.getBank()).isEqualTo(bankBack);

        card.bank(null);
        assertThat(card.getBank()).isNull();
    }
}
