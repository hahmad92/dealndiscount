package pk.dealndiscount.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.dealndiscount.domain.Deal;
import pk.dealndiscount.repository.DealRepository;
import pk.dealndiscount.service.DealService;
import pk.dealndiscount.service.dto.DealDTO;
import pk.dealndiscount.service.mapper.DealMapper;

/**
 * Service Implementation for managing {@link pk.dealndiscount.domain.Deal}.
 */
@Service
@Transactional
public class DealServiceImpl implements DealService {

    private static final Logger log = LoggerFactory.getLogger(DealServiceImpl.class);

    private final DealRepository dealRepository;

    private final DealMapper dealMapper;

    public DealServiceImpl(DealRepository dealRepository, DealMapper dealMapper) {
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
    }

    @Override
    public DealDTO save(DealDTO dealDTO) {
        log.debug("Request to save Deal : {}", dealDTO);
        Deal deal = dealMapper.toEntity(dealDTO);
        deal = dealRepository.save(deal);
        return dealMapper.toDto(deal);
    }

    @Override
    public DealDTO update(DealDTO dealDTO) {
        log.debug("Request to update Deal : {}", dealDTO);
        Deal deal = dealMapper.toEntity(dealDTO);
        deal = dealRepository.save(deal);
        return dealMapper.toDto(deal);
    }

    @Override
    public Optional<DealDTO> partialUpdate(DealDTO dealDTO) {
        log.debug("Request to partially update Deal : {}", dealDTO);

        return dealRepository
            .findById(dealDTO.getId())
            .map(existingDeal -> {
                dealMapper.partialUpdate(existingDeal, dealDTO);

                return existingDeal;
            })
            .map(dealRepository::save)
            .map(dealMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DealDTO> findOne(Long id) {
        log.debug("Request to get Deal : {}", id);
        return dealRepository.findById(id).map(dealMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Deal : {}", id);
        dealRepository.deleteById(id);
    }
}
