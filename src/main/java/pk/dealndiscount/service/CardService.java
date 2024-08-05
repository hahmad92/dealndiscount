package pk.dealndiscount.service;

import java.util.Optional;
import pk.dealndiscount.service.dto.CardDTO;

/**
 * Service Interface for managing {@link pk.dealndiscount.domain.Card}.
 */
public interface CardService {
    /**
     * Save a card.
     *
     * @param cardDTO the entity to save.
     * @return the persisted entity.
     */
    CardDTO save(CardDTO cardDTO);

    /**
     * Updates a card.
     *
     * @param cardDTO the entity to update.
     * @return the persisted entity.
     */
    CardDTO update(CardDTO cardDTO);

    /**
     * Partially updates a card.
     *
     * @param cardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CardDTO> partialUpdate(CardDTO cardDTO);

    /**
     * Get the "id" card.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CardDTO> findOne(Long id);

    /**
     * Delete the "id" card.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
