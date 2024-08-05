package pk.dealndiscount.service.mapper;

import org.mapstruct.*;
import pk.dealndiscount.domain.Card;
import pk.dealndiscount.domain.Deal;
import pk.dealndiscount.domain.Store;
import pk.dealndiscount.service.dto.CardDTO;
import pk.dealndiscount.service.dto.DealDTO;
import pk.dealndiscount.service.dto.StoreDTO;

/**
 * Mapper for the entity {@link Deal} and its DTO {@link DealDTO}.
 */
@Mapper(componentModel = "spring")
public interface DealMapper extends EntityMapper<DealDTO, Deal> {
    @Mapping(target = "card", source = "card", qualifiedByName = "cardId")
    @Mapping(target = "store", source = "store", qualifiedByName = "storeId")
    DealDTO toDto(Deal s);

    @Named("cardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CardDTO toDtoCardId(Card card);

    @Named("storeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StoreDTO toDtoStoreId(Store store);
}
