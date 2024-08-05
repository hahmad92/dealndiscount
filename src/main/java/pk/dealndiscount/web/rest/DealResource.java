package pk.dealndiscount.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pk.dealndiscount.repository.DealRepository;
import pk.dealndiscount.service.DealQueryService;
import pk.dealndiscount.service.DealService;
import pk.dealndiscount.service.criteria.DealCriteria;
import pk.dealndiscount.service.dto.DealDTO;
import pk.dealndiscount.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pk.dealndiscount.domain.Deal}.
 */
@RestController
@RequestMapping("/api/deals")
public class DealResource {

    private static final Logger log = LoggerFactory.getLogger(DealResource.class);

    private static final String ENTITY_NAME = "deal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DealService dealService;

    private final DealRepository dealRepository;

    private final DealQueryService dealQueryService;

    public DealResource(DealService dealService, DealRepository dealRepository, DealQueryService dealQueryService) {
        this.dealService = dealService;
        this.dealRepository = dealRepository;
        this.dealQueryService = dealQueryService;
    }

    /**
     * {@code POST  /deals} : Create a new deal.
     *
     * @param dealDTO the dealDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dealDTO, or with status {@code 400 (Bad Request)} if the deal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DealDTO> createDeal(@Valid @RequestBody DealDTO dealDTO) throws URISyntaxException {
        log.debug("REST request to save Deal : {}", dealDTO);
        if (dealDTO.getId() != null) {
            throw new BadRequestAlertException("A new deal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dealDTO = dealService.save(dealDTO);
        return ResponseEntity.created(new URI("/api/deals/" + dealDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dealDTO.getId().toString()))
            .body(dealDTO);
    }

    /**
     * {@code PUT  /deals/:id} : Updates an existing deal.
     *
     * @param id the id of the dealDTO to save.
     * @param dealDTO the dealDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealDTO,
     * or with status {@code 400 (Bad Request)} if the dealDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dealDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DealDTO> updateDeal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DealDTO dealDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Deal : {}, {}", id, dealDTO);
        if (dealDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dealDTO = dealService.update(dealDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dealDTO.getId().toString()))
            .body(dealDTO);
    }

    /**
     * {@code PATCH  /deals/:id} : Partial updates given fields of an existing deal, field will ignore if it is null
     *
     * @param id the id of the dealDTO to save.
     * @param dealDTO the dealDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealDTO,
     * or with status {@code 400 (Bad Request)} if the dealDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dealDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dealDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DealDTO> partialUpdateDeal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DealDTO dealDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deal partially : {}, {}", id, dealDTO);
        if (dealDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DealDTO> result = dealService.partialUpdate(dealDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dealDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /deals} : get all the deals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DealDTO>> getAllDeals(
        DealCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Deals by criteria: {}", criteria);

        Page<DealDTO> page = dealQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deals/count} : count all the deals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDeals(DealCriteria criteria) {
        log.debug("REST request to count Deals by criteria: {}", criteria);
        return ResponseEntity.ok().body(dealQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /deals/:id} : get the "id" deal.
     *
     * @param id the id of the dealDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dealDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DealDTO> getDeal(@PathVariable("id") Long id) {
        log.debug("REST request to get Deal : {}", id);
        Optional<DealDTO> dealDTO = dealService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dealDTO);
    }

    /**
     * {@code DELETE  /deals/:id} : delete the "id" deal.
     *
     * @param id the id of the dealDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable("id") Long id) {
        log.debug("REST request to delete Deal : {}", id);
        dealService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
