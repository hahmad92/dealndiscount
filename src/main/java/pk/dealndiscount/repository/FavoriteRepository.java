package pk.dealndiscount.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pk.dealndiscount.domain.Favorite;

/**
 * Spring Data JPA repository for the Favorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {}
