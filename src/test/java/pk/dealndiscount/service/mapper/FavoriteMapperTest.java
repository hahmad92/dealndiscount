package pk.dealndiscount.service.mapper;

import static pk.dealndiscount.domain.FavoriteAsserts.*;
import static pk.dealndiscount.domain.FavoriteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavoriteMapperTest {

    private FavoriteMapper favoriteMapper;

    @BeforeEach
    void setUp() {
        favoriteMapper = new FavoriteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFavoriteSample1();
        var actual = favoriteMapper.toEntity(favoriteMapper.toDto(expected));
        assertFavoriteAllPropertiesEquals(expected, actual);
    }
}
