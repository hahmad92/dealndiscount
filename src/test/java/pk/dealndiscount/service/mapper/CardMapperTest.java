package pk.dealndiscount.service.mapper;

import static pk.dealndiscount.domain.CardAsserts.*;
import static pk.dealndiscount.domain.CardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardMapperTest {

    private CardMapper cardMapper;

    @BeforeEach
    void setUp() {
        cardMapper = new CardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCardSample1();
        var actual = cardMapper.toEntity(cardMapper.toDto(expected));
        assertCardAllPropertiesEquals(expected, actual);
    }
}
