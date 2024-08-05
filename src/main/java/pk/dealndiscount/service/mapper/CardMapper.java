package pk.dealndiscount.service.mapper;

import org.mapstruct.*;
import pk.dealndiscount.domain.Bank;
import pk.dealndiscount.domain.Card;
import pk.dealndiscount.service.dto.BankDTO;
import pk.dealndiscount.service.dto.CardDTO;

/**
 * Mapper for the entity {@link Card} and its DTO {@link CardDTO}.
 */
@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardDTO, Card> {
    @Mapping(target = "bank", source = "bank", qualifiedByName = "bankId")
    CardDTO toDto(Card s);

    @Named("bankId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankDTO toDtoBankId(Bank bank);
}
