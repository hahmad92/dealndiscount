package pk.dealndiscount.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pk.dealndiscount.service.dto.StoreDTO;

/**
 * Service Interface for managing {@link pk.dealndiscount.domain.Store}.
 */
public interface StoreService {
    /**
     * Save a store.
     *
     * @param storeDTO the entity to save.
     * @return the persisted entity.
     */
    StoreDTO save(StoreDTO storeDTO);

    /**
     * Updates a store.
     *
     * @param storeDTO the entity to update.
     * @return the persisted entity.
     */
    StoreDTO update(StoreDTO storeDTO);

    /**
     * Partially updates a store.
     *
     * @param storeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StoreDTO> partialUpdate(StoreDTO storeDTO);

    /**
     * Get all the stores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StoreDTO> findAll(Pageable pageable);

    /**
     * Get the "id" store.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StoreDTO> findOne(Long id);

    /**
     * Delete the "id" store.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
