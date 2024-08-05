package pk.dealndiscount.service;

import java.util.List;
import java.util.Optional;
import pk.dealndiscount.domain.ExtendedUser;

/**
 * Service Interface for managing {@link pk.dealndiscount.domain.ExtendedUser}.
 */
public interface ExtendedUserService {
    /**
     * Save a extendedUser.
     *
     * @param extendedUser the entity to save.
     * @return the persisted entity.
     */
    ExtendedUser save(ExtendedUser extendedUser);

    /**
     * Updates a extendedUser.
     *
     * @param extendedUser the entity to update.
     * @return the persisted entity.
     */
    ExtendedUser update(ExtendedUser extendedUser);

    /**
     * Partially updates a extendedUser.
     *
     * @param extendedUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExtendedUser> partialUpdate(ExtendedUser extendedUser);

    /**
     * Get all the extendedUsers.
     *
     * @return the list of entities.
     */
    List<ExtendedUser> findAll();

    /**
     * Get the "id" extendedUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExtendedUser> findOne(Long id);

    /**
     * Delete the "id" extendedUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
