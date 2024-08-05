package pk.dealndiscount.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pk.dealndiscount.domain.Store;

/**
 * Spring Data JPA repository for the Store entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {}
