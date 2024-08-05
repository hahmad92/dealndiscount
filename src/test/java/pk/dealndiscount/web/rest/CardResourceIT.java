package pk.dealndiscount.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pk.dealndiscount.domain.CardAsserts.*;
import static pk.dealndiscount.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import pk.dealndiscount.domain.Bank;
import pk.dealndiscount.domain.Card;
import pk.dealndiscount.domain.enumeration.CardCategory;
import pk.dealndiscount.domain.enumeration.CardNetwork;
import pk.dealndiscount.repository.CardRepository;
import pk.dealndiscount.service.dto.CardDTO;
import pk.dealndiscount.service.mapper.CardMapper;

/**
 * Integration tests for the {@link CardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CardResourceIT {

    private static final String DEFAULT_CARD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NAME = "BBBBBBBBBB";

    private static final CardCategory DEFAULT_CATEGORY = CardCategory.CREDIT;
    private static final CardCategory UPDATED_CATEGORY = CardCategory.DEBIT;

    private static final CardNetwork DEFAULT_CARD_NETWORK = CardNetwork.VISA;
    private static final CardNetwork UPDATED_CARD_NETWORK = CardNetwork.MASTERCARD;

    private static final String ENTITY_API_URL = "/api/cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardMockMvc;

    private Card card;

    private Card insertedCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {
        Card card = new Card().cardName(DEFAULT_CARD_NAME).category(DEFAULT_CATEGORY).cardNetwork(DEFAULT_CARD_NETWORK);
        return card;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createUpdatedEntity(EntityManager em) {
        Card card = new Card().cardName(UPDATED_CARD_NAME).category(UPDATED_CATEGORY).cardNetwork(UPDATED_CARD_NETWORK);
        return card;
    }

    @BeforeEach
    public void initTest() {
        card = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCard != null) {
            cardRepository.delete(insertedCard);
            insertedCard = null;
        }
    }

    @Test
    @Transactional
    void createCard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);
        var returnedCardDTO = om.readValue(
            restCardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CardDTO.class
        );

        // Validate the Card in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCard = cardMapper.toEntity(returnedCardDTO);
        assertCardUpdatableFieldsEquals(returnedCard, getPersistedCard(returnedCard));

        insertedCard = returnedCard;
    }

    @Test
    @Transactional
    void createCardWithExistingId() throws Exception {
        // Create the Card with an existing ID
        card.setId(1L);
        CardDTO cardDTO = cardMapper.toDto(card);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCardNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        card.setCardName(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.toDto(card);

        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        card.setCategory(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.toDto(card);

        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardNetworkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        card.setCardNetwork(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.toDto(card);

        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCards() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardName").value(hasItem(DEFAULT_CARD_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].cardNetwork").value(hasItem(DEFAULT_CARD_NETWORK.toString())));
    }

    @Test
    @Transactional
    void getCard() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc
            .perform(get(ENTITY_API_URL_ID, card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.cardName").value(DEFAULT_CARD_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.cardNetwork").value(DEFAULT_CARD_NETWORK.toString()));
    }

    @Test
    @Transactional
    void getCardsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        Long id = card.getId();

        defaultCardFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCardFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCardFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardsByCardNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where cardName equals to
        defaultCardFiltering("cardName.equals=" + DEFAULT_CARD_NAME, "cardName.equals=" + UPDATED_CARD_NAME);
    }

    @Test
    @Transactional
    void getAllCardsByCardNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where cardName in
        defaultCardFiltering("cardName.in=" + DEFAULT_CARD_NAME + "," + UPDATED_CARD_NAME, "cardName.in=" + UPDATED_CARD_NAME);
    }

    @Test
    @Transactional
    void getAllCardsByCardNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where cardName is not null
        defaultCardFiltering("cardName.specified=true", "cardName.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByCardNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where cardName contains
        defaultCardFiltering("cardName.contains=" + DEFAULT_CARD_NAME, "cardName.contains=" + UPDATED_CARD_NAME);
    }

    @Test
    @Transactional
    void getAllCardsByCardNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where cardName does not contain
        defaultCardFiltering("cardName.doesNotContain=" + UPDATED_CARD_NAME, "cardName.doesNotContain=" + DEFAULT_CARD_NAME);
    }

    @Test
    @Transactional
    void getAllCardsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where category equals to
        defaultCardFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCardsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where category in
        defaultCardFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCardsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where category is not null
        defaultCardFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByCardNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where cardNetwork equals to
        defaultCardFiltering("cardNetwork.equals=" + DEFAULT_CARD_NETWORK, "cardNetwork.equals=" + UPDATED_CARD_NETWORK);
    }

    @Test
    @Transactional
    void getAllCardsByCardNetworkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where cardNetwork in
        defaultCardFiltering(
            "cardNetwork.in=" + DEFAULT_CARD_NETWORK + "," + UPDATED_CARD_NETWORK,
            "cardNetwork.in=" + UPDATED_CARD_NETWORK
        );
    }

    @Test
    @Transactional
    void getAllCardsByCardNetworkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where cardNetwork is not null
        defaultCardFiltering("cardNetwork.specified=true", "cardNetwork.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByBankIsEqualToSomething() throws Exception {
        Bank bank;
        if (TestUtil.findAll(em, Bank.class).isEmpty()) {
            cardRepository.saveAndFlush(card);
            bank = BankResourceIT.createEntity(em);
        } else {
            bank = TestUtil.findAll(em, Bank.class).get(0);
        }
        em.persist(bank);
        em.flush();
        card.setBank(bank);
        cardRepository.saveAndFlush(card);
        Long bankId = bank.getId();
        // Get all the cardList where bank equals to bankId
        defaultCardShouldBeFound("bankId.equals=" + bankId);

        // Get all the cardList where bank equals to (bankId + 1)
        defaultCardShouldNotBeFound("bankId.equals=" + (bankId + 1));
    }

    private void defaultCardFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCardShouldBeFound(shouldBeFound);
        defaultCardShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardShouldBeFound(String filter) throws Exception {
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardName").value(hasItem(DEFAULT_CARD_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].cardNetwork").value(hasItem(DEFAULT_CARD_NETWORK.toString())));

        // Check, that the count call also returns 1
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardShouldNotBeFound(String filter) throws Exception {
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCard() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the card
        Card updatedCard = cardRepository.findById(card.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCard are not directly saved in db
        em.detach(updatedCard);
        updatedCard.cardName(UPDATED_CARD_NAME).category(UPDATED_CATEGORY).cardNetwork(UPDATED_CARD_NETWORK);
        CardDTO cardDTO = cardMapper.toDto(updatedCard);

        restCardMockMvc
            .perform(put(ENTITY_API_URL_ID, cardDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDTO)))
            .andExpect(status().isOk());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCardToMatchAllProperties(updatedCard);
    }

    @Test
    @Transactional
    void putNonExistingCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(put(ENTITY_API_URL_ID, cardDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCardWithPatch() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        partialUpdatedCard.cardName(UPDATED_CARD_NAME).category(UPDATED_CATEGORY);

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCardUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCard, card), getPersistedCard(card));
    }

    @Test
    @Transactional
    void fullUpdateCardWithPatch() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        partialUpdatedCard.cardName(UPDATED_CARD_NAME).category(UPDATED_CATEGORY).cardNetwork(UPDATED_CARD_NETWORK);

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCardUpdatableFieldsEquals(partialUpdatedCard, getPersistedCard(partialUpdatedCard));
    }

    @Test
    @Transactional
    void patchNonExistingCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCard() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the card
        restCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, card.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cardRepository.count();
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

    protected Card getPersistedCard(Card card) {
        return cardRepository.findById(card.getId()).orElseThrow();
    }

    protected void assertPersistedCardToMatchAllProperties(Card expectedCard) {
        assertCardAllPropertiesEquals(expectedCard, getPersistedCard(expectedCard));
    }

    protected void assertPersistedCardToMatchUpdatableProperties(Card expectedCard) {
        assertCardAllUpdatablePropertiesEquals(expectedCard, getPersistedCard(expectedCard));
    }
}
