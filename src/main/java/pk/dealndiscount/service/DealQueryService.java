package pk.dealndiscount.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.dealndiscount.domain.*; // for static metamodels
import pk.dealndiscount.domain.Deal;
import pk.dealndiscount.repository.DealRepository;
import pk.dealndiscount.service.criteria.DealCriteria;
import pk.dealndiscount.service.dto.DealDTO;
import pk.dealndiscount.service.mapper.DealMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Deal} entities in the database.
 * The main input is a {@link DealCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DealDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DealQueryService extends QueryService<Deal> {

    private static final Logger log = LoggerFactory.getLogger(DealQueryService.class);

    private final DealRepository dealRepository;

    private final DealMapper dealMapper;

    public DealQueryService(DealRepository dealRepository, DealMapper dealMapper) {
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
    }

    /**
     * Return a {@link Page} of {@link DealDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DealDTO> findByCriteria(DealCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Deal> specification = createSpecification(criteria);
        return dealRepository.findAll(specification, page).map(dealMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DealCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Deal> specification = createSpecification(criteria);
        return dealRepository.count(specification);
    }

    /**
     * Function to convert {@link DealCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Deal> createSpecification(DealCriteria criteria) {
        Specification<Deal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Deal_.id));
            }
            if (criteria.getDiscountPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountPercentage(), Deal_.discountPercentage));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Deal_.description));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Deal_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Deal_.endDate));
            }
            if (criteria.getIsDealActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDealActive(), Deal_.isDealActive));
            }
            if (criteria.getCardId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCardId(), root -> root.join(Deal_.card, JoinType.LEFT).get(Card_.id))
                );
            }
            if (criteria.getStoreId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getStoreId(), root -> root.join(Deal_.store, JoinType.LEFT).get(Store_.id))
                );
            }
        }
        return specification;
    }
}
