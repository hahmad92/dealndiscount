package pk.dealndiscount.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pk.dealndiscount.domain.BankTestSamples.*;

import org.junit.jupiter.api.Test;
import pk.dealndiscount.web.rest.TestUtil;

class BankTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bank.class);
        Bank bank1 = getBankSample1();
        Bank bank2 = new Bank();
        assertThat(bank1).isNotEqualTo(bank2);

        bank2.setId(bank1.getId());
        assertThat(bank1).isEqualTo(bank2);

        bank2 = getBankSample2();
        assertThat(bank1).isNotEqualTo(bank2);
    }
}
