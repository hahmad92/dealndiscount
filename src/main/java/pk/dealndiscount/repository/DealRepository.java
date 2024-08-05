package pk.dealndiscount.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pk.dealndiscount.domain.Deal;

/**
 * Spring Data JPA repository for the Deal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DealRepository extends JpaRepository<Deal, Long>, JpaSpecificationExecutor<Deal> {}
