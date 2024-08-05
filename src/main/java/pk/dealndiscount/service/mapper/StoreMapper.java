package pk.dealndiscount.service.mapper;

import org.mapstruct.*;
import pk.dealndiscount.domain.City;
import pk.dealndiscount.domain.Store;
import pk.dealndiscount.service.dto.CityDTO;
import pk.dealndiscount.service.dto.StoreDTO;

/**
 * Mapper for the entity {@link Store} and its DTO {@link StoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface StoreMapper extends EntityMapper<StoreDTO, Store> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityId")
    StoreDTO toDto(Store s);

    @Named("cityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CityDTO toDtoCityId(City city);
}
