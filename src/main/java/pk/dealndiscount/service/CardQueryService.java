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
import pk.dealndiscount.domain.Card;
import pk.dealndiscount.repository.CardRepository;
import pk.dealndiscount.service.criteria.CardCriteria;
import pk.dealndiscount.service.dto.CardDTO;
import pk.dealndiscount.service.mapper.CardMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Card} entities in the database.
 * The main input is a {@link CardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CardDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardQueryService extends QueryService<Card> {

    private static final Logger log = LoggerFactory.getLogger(CardQueryService.class);

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    public CardQueryService(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    /**
     * Return a {@link Page} of {@link CardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardDTO> findByCriteria(CardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Card> specification = createSpecification(criteria);
        return cardRepository.findAll(specification, page).map(cardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Card> specification = createSpecification(criteria);
        return cardRepository.count(specification);
    }

    /**
     * Function to convert {@link CardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Card> createSpecification(CardCriteria criteria) {
        Specification<Card> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Card_.id));
            }
            if (criteria.getCardName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCardName(), Card_.cardName));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildSpecification(criteria.getCategory(), Card_.category));
            }
            if (criteria.getCardNetwork() != null) {
                specification = specification.and(buildSpecification(criteria.getCardNetwork(), Card_.cardNetwork));
            }
            if (criteria.getBankId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBankId(), root -> root.join(Card_.bank, JoinType.LEFT).get(Bank_.id))
                );
            }
        }
        return specification;
    }
}
