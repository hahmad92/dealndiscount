package pk.dealndiscount.service.mapper;

import org.mapstruct.*;
import pk.dealndiscount.domain.Favorite;
import pk.dealndiscount.service.dto.FavoriteDTO;

/**
 * Mapper for the entity {@link Favorite} and its DTO {@link FavoriteDTO}.
 */
@Mapper(componentModel = "spring")
public interface FavoriteMapper extends EntityMapper<FavoriteDTO, Favorite> {}
