package pk.dealndiscount.service.mapper;

import org.mapstruct.*;
import pk.dealndiscount.domain.Bank;
import pk.dealndiscount.service.dto.BankDTO;

/**
 * Mapper for the entity {@link Bank} and its DTO {@link BankDTO}.
 */
@Mapper(componentModel = "spring")
public interface BankMapper extends EntityMapper<BankDTO, Bank> {}
