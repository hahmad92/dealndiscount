package pk.dealndiscount.service.mapper;

import static pk.dealndiscount.domain.BankAsserts.*;
import static pk.dealndiscount.domain.BankTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankMapperTest {

    private BankMapper bankMapper;

    @BeforeEach
    void setUp() {
        bankMapper = new BankMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBankSample1();
        var actual = bankMapper.toEntity(bankMapper.toDto(expected));
        assertBankAllPropertiesEquals(expected, actual);
    }
}
