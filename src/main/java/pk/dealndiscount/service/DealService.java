package pk.dealndiscount.service;

import java.util.Optional;
import pk.dealndiscount.service.dto.DealDTO;

/**
 * Service Interface for managing {@link pk.dealndiscount.domain.Deal}.
 */
public interface DealService {
    /**
     * Save a deal.
     *
     * @param dealDTO the entity to save.
     * @return the persisted entity.
     */
    DealDTO save(DealDTO dealDTO);

    /**
     * Updates a deal.
     *
     * @param dealDTO the entity to update.
     * @return the persisted entity.
     */
    DealDTO update(DealDTO dealDTO);

    /**
     * Partially updates a deal.
     *
     * @param dealDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DealDTO> partialUpdate(DealDTO dealDTO);

    /**
     * Get the "id" deal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DealDTO> findOne(Long id);

    /**
     * Delete the "id" deal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
