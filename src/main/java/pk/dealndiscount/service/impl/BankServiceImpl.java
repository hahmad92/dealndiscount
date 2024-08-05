package pk.dealndiscount.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.dealndiscount.domain.Bank;
import pk.dealndiscount.repository.BankRepository;
import pk.dealndiscount.service.BankService;
import pk.dealndiscount.service.dto.BankDTO;
import pk.dealndiscount.service.mapper.BankMapper;

/**
 * Service Implementation for managing {@link pk.dealndiscount.domain.Bank}.
 */
@Service
@Transactional
public class BankServiceImpl implements BankService {

    private static final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    private final BankRepository bankRepository;

    private final BankMapper bankMapper;

    public BankServiceImpl(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    @Override
    public BankDTO save(BankDTO bankDTO) {
        log.debug("Request to save Bank : {}", bankDTO);
        Bank bank = bankMapper.toEntity(bankDTO);
        bank = bankRepository.save(bank);
        return bankMapper.toDto(bank);
    }

    @Override
    public BankDTO update(BankDTO bankDTO) {
        log.debug("Request to update Bank : {}", bankDTO);
        Bank bank = bankMapper.toEntity(bankDTO);
        bank = bankRepository.save(bank);
        return bankMapper.toDto(bank);
    }

    @Override
    public Optional<BankDTO> partialUpdate(BankDTO bankDTO) {
        log.debug("Request to partially update Bank : {}", bankDTO);

        return bankRepository
            .findById(bankDTO.getId())
            .map(existingBank -> {
                bankMapper.partialUpdate(existingBank, bankDTO);

                return existingBank;
            })
            .map(bankRepository::save)
            .map(bankMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Banks");
        return bankRepository.findAll(pageable).map(bankMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankDTO> findOne(Long id) {
        log.debug("Request to get Bank : {}", id);
        return bankRepository.findById(id).map(bankMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bank : {}", id);
        bankRepository.deleteById(id);
    }
}
