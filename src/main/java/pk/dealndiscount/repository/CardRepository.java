package pk.dealndiscount.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pk.dealndiscount.domain.Card;

/**
 * Spring Data JPA repository for the Card entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {}
