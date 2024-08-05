package pk.dealndiscount.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.dealndiscount.domain.Store;
import pk.dealndiscount.repository.StoreRepository;
import pk.dealndiscount.service.StoreService;
import pk.dealndiscount.service.dto.StoreDTO;
import pk.dealndiscount.service.mapper.StoreMapper;

/**
 * Service Implementation for managing {@link pk.dealndiscount.domain.Store}.
 */
@Service
@Transactional
public class StoreServiceImpl implements StoreService {

    private static final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    public StoreServiceImpl(StoreRepository storeRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
    }

    @Override
    public StoreDTO save(StoreDTO storeDTO) {
        log.debug("Request to save Store : {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        store = storeRepository.save(store);
        return storeMapper.toDto(store);
    }

    @Override
    public StoreDTO update(StoreDTO storeDTO) {
        log.debug("Request to update Store : {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        store = storeRepository.save(store);
        return storeMapper.toDto(store);
    }

    @Override
    public Optional<StoreDTO> partialUpdate(StoreDTO storeDTO) {
        log.debug("Request to partially update Store : {}", storeDTO);

        return storeRepository
            .findById(storeDTO.getId())
            .map(existingStore -> {
                storeMapper.partialUpdate(existingStore, storeDTO);

                return existingStore;
            })
            .map(storeRepository::save)
            .map(storeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stores");
        return storeRepository.findAll(pageable).map(storeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StoreDTO> findOne(Long id) {
        log.debug("Request to get Store : {}", id);
        return storeRepository.findById(id).map(storeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Store : {}", id);
        storeRepository.deleteById(id);
    }
}
