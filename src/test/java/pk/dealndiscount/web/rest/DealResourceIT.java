package pk.dealndiscount.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pk.dealndiscount.domain.DealAsserts.*;
import static pk.dealndiscount.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pk.dealndiscount.IntegrationTest;
import pk.dealndiscount.domain.Card;
import pk.dealndiscount.domain.Deal;
import pk.dealndiscount.domain.Store;
import pk.dealndiscount.repository.DealRepository;
import pk.dealndiscount.service.dto.DealDTO;
import pk.dealndiscount.service.mapper.DealMapper;

/**
 * Integration tests for the {@link DealResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DealResourceIT {

    private static final Integer DEFAULT_DISCOUNT_PERCENTAGE = 1;
    private static final Integer UPDATED_DISCOUNT_PERCENTAGE = 2;
    private static final Integer SMALLER_DISCOUNT_PERCENTAGE = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_DEAL_ACTIVE = false;
    private static final Boolean UPDATED_IS_DEAL_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/deals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private DealMapper dealMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealMockMvc;

    private Deal deal;

    private Deal insertedDeal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deal createEntity(EntityManager em) {
        Deal deal = new Deal()
            .discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isDealActive(DEFAULT_IS_DEAL_ACTIVE);
        return deal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deal createUpdatedEntity(EntityManager em) {
        Deal deal = new Deal()
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isDealActive(UPDATED_IS_DEAL_ACTIVE);
        return deal;
    }

    @BeforeEach
    public void initTest() {
        deal = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDeal != null) {
            dealRepository.delete(insertedDeal);
            insertedDeal = null;
        }
    }

    @Test
    @Transactional
    void createDeal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);
        var returnedDealDTO = om.readValue(
            restDealMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DealDTO.class
        );

        // Validate the Deal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDeal = dealMapper.toEntity(returnedDealDTO);
        assertDealUpdatableFieldsEquals(returnedDeal, getPersistedDeal(returnedDeal));

        insertedDeal = returnedDeal;
    }

    @Test
    @Transactional
    void createDealWithExistingId() throws Exception {
        // Create the Deal with an existing ID
        deal.setId(1L);
        DealDTO dealDTO = dealMapper.toDto(deal);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDiscountPercentageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deal.setDiscountPercentage(null);

        // Create the Deal, which fails.
        DealDTO dealDTO = dealMapper.toDto(deal);

        restDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDealActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deal.setIsDealActive(null);

        // Create the Deal, which fails.
        DealDTO dealDTO = dealMapper.toDto(deal);

        restDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeals() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deal.getId().intValue())))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isDealActive").value(hasItem(DEFAULT_IS_DEAL_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getDeal() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get the deal
        restDealMockMvc
            .perform(get(ENTITY_API_URL_ID, deal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deal.getId().intValue()))
            .andExpect(jsonPath("$.discountPercentage").value(DEFAULT_DISCOUNT_PERCENTAGE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isDealActive").value(DEFAULT_IS_DEAL_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getDealsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        Long id = deal.getId();

        defaultDealFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDealFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDealFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDealsByDiscountPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where discountPercentage equals to
        defaultDealFiltering(
            "discountPercentage.equals=" + DEFAULT_DISCOUNT_PERCENTAGE,
            "discountPercentage.equals=" + UPDATED_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllDealsByDiscountPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where discountPercentage in
        defaultDealFiltering(
            "discountPercentage.in=" + DEFAULT_DISCOUNT_PERCENTAGE + "," + UPDATED_DISCOUNT_PERCENTAGE,
            "discountPercentage.in=" + UPDATED_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllDealsByDiscountPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where discountPercentage is not null
        defaultDealFiltering("discountPercentage.specified=true", "discountPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByDiscountPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where discountPercentage is greater than or equal to
        defaultDealFiltering(
            "discountPercentage.greaterThanOrEqual=" + DEFAULT_DISCOUNT_PERCENTAGE,
            "discountPercentage.greaterThanOrEqual=" + (DEFAULT_DISCOUNT_PERCENTAGE + 1)
        );
    }

    @Test
    @Transactional
    void getAllDealsByDiscountPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where discountPercentage is less than or equal to
        defaultDealFiltering(
            "discountPercentage.lessThanOrEqual=" + DEFAULT_DISCOUNT_PERCENTAGE,
            "discountPercentage.lessThanOrEqual=" + SMALLER_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllDealsByDiscountPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where discountPercentage is less than
        defaultDealFiltering(
            "discountPercentage.lessThan=" + (DEFAULT_DISCOUNT_PERCENTAGE + 1),
            "discountPercentage.lessThan=" + DEFAULT_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllDealsByDiscountPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where discountPercentage is greater than
        defaultDealFiltering(
            "discountPercentage.greaterThan=" + SMALLER_DISCOUNT_PERCENTAGE,
            "discountPercentage.greaterThan=" + DEFAULT_DISCOUNT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllDealsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where description equals to
        defaultDealFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDealsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where description in
        defaultDealFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDealsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where description is not null
        defaultDealFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where description contains
        defaultDealFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDealsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where description does not contain
        defaultDealFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDealsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where startDate equals to
        defaultDealFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDealsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where startDate in
        defaultDealFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDealsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where startDate is not null
        defaultDealFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where endDate equals to
        defaultDealFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDealsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where endDate in
        defaultDealFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDealsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where endDate is not null
        defaultDealFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByIsDealActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where isDealActive equals to
        defaultDealFiltering("isDealActive.equals=" + DEFAULT_IS_DEAL_ACTIVE, "isDealActive.equals=" + UPDATED_IS_DEAL_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDealsByIsDealActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where isDealActive in
        defaultDealFiltering(
            "isDealActive.in=" + DEFAULT_IS_DEAL_ACTIVE + "," + UPDATED_IS_DEAL_ACTIVE,
            "isDealActive.in=" + UPDATED_IS_DEAL_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllDealsByIsDealActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        // Get all the dealList where isDealActive is not null
        defaultDealFiltering("isDealActive.specified=true", "isDealActive.specified=false");
    }

    @Test
    @Transactional
    void getAllDealsByCardIsEqualToSomething() throws Exception {
        Card card;
        if (TestUtil.findAll(em, Card.class).isEmpty()) {
            dealRepository.saveAndFlush(deal);
            card = CardResourceIT.createEntity(em);
        } else {
            card = TestUtil.findAll(em, Card.class).get(0);
        }
        em.persist(card);
        em.flush();
        deal.setCard(card);
        dealRepository.saveAndFlush(deal);
        Long cardId = card.getId();
        // Get all the dealList where card equals to cardId
        defaultDealShouldBeFound("cardId.equals=" + cardId);

        // Get all the dealList where card equals to (cardId + 1)
        defaultDealShouldNotBeFound("cardId.equals=" + (cardId + 1));
    }

    @Test
    @Transactional
    void getAllDealsByStoreIsEqualToSomething() throws Exception {
        Store store;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            dealRepository.saveAndFlush(deal);
            store = StoreResourceIT.createEntity(em);
        } else {
            store = TestUtil.findAll(em, Store.class).get(0);
        }
        em.persist(store);
        em.flush();
        deal.setStore(store);
        dealRepository.saveAndFlush(deal);
        Long storeId = store.getId();
        // Get all the dealList where store equals to storeId
        defaultDealShouldBeFound("storeId.equals=" + storeId);

        // Get all the dealList where store equals to (storeId + 1)
        defaultDealShouldNotBeFound("storeId.equals=" + (storeId + 1));
    }

    private void defaultDealFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDealShouldBeFound(shouldBeFound);
        defaultDealShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDealShouldBeFound(String filter) throws Exception {
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deal.getId().intValue())))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isDealActive").value(hasItem(DEFAULT_IS_DEAL_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDealShouldNotBeFound(String filter) throws Exception {
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeal() throws Exception {
        // Get the deal
        restDealMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeal() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deal
        Deal updatedDeal = dealRepository.findById(deal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeal are not directly saved in db
        em.detach(updatedDeal);
        updatedDeal
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isDealActive(UPDATED_IS_DEAL_ACTIVE);
        DealDTO dealDTO = dealMapper.toDto(updatedDeal);

        restDealMockMvc
            .perform(put(ENTITY_API_URL_ID, dealDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isOk());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDealToMatchAllProperties(updatedDeal);
    }

    @Test
    @Transactional
    void putNonExistingDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(put(ENTITY_API_URL_ID, dealDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDealWithPatch() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deal using partial update
        Deal partialUpdatedDeal = new Deal();
        partialUpdatedDeal.setId(deal.getId());

        partialUpdatedDeal
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .isDealActive(UPDATED_IS_DEAL_ACTIVE);

        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeal))
            )
            .andExpect(status().isOk());

        // Validate the Deal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDeal, deal), getPersistedDeal(deal));
    }

    @Test
    @Transactional
    void fullUpdateDealWithPatch() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deal using partial update
        Deal partialUpdatedDeal = new Deal();
        partialUpdatedDeal.setId(deal.getId());

        partialUpdatedDeal
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isDealActive(UPDATED_IS_DEAL_ACTIVE);

        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeal))
            )
            .andExpect(status().isOk());

        // Validate the Deal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealUpdatableFieldsEquals(partialUpdatedDeal, getPersistedDeal(partialUpdatedDeal));
    }

    @Test
    @Transactional
    void patchNonExistingDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dealDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deal.setId(longCount.incrementAndGet());

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dealDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeal() throws Exception {
        // Initialize the database
        insertedDeal = dealRepository.saveAndFlush(deal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the deal
        restDealMockMvc
            .perform(delete(ENTITY_API_URL_ID, deal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dealRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Deal getPersistedDeal(Deal deal) {
        return dealRepository.findById(deal.getId()).orElseThrow();
    }

    protected void assertPersistedDealToMatchAllProperties(Deal expectedDeal) {
        assertDealAllPropertiesEquals(expectedDeal, getPersistedDeal(expectedDeal));
    }

    protected void assertPersistedDealToMatchUpdatableProperties(Deal expectedDeal) {
        assertDealAllUpdatablePropertiesEquals(expectedDeal, getPersistedDeal(expectedDeal));
    }
}
