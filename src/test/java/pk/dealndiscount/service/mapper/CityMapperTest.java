package pk.dealndiscount.service.mapper;

import static pk.dealndiscount.domain.CityAsserts.*;
import static pk.dealndiscount.domain.CityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CityMapperTest {

    private CityMapper cityMapper;

    @BeforeEach
    void setUp() {
        cityMapper = new CityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCitySample1();
        var actual = cityMapper.toEntity(cityMapper.toDto(expected));
        assertCityAllPropertiesEquals(expected, actual);
    }
}
