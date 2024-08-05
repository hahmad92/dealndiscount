package pk.dealndiscount.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pk.dealndiscount.domain.BankAsserts.*;
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
import pk.dealndiscount.domain.enumeration.BankType;
import pk.dealndiscount.repository.BankRepository;
import pk.dealndiscount.service.dto.BankDTO;
import pk.dealndiscount.service.mapper.BankMapper;

/**
 * Integration tests for the {@link BankResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BankResourceIT {

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final BankType DEFAULT_BANK_TYPE = BankType.CONVENTIONAL;
    private static final BankType UPDATED_BANK_TYPE = BankType.ISLAMIC;

    private static final String ENTITY_API_URL = "/api/banks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankMockMvc;

    private Bank bank;

    private Bank insertedBank;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createEntity(EntityManager em) {
        Bank bank = new Bank().bankName(DEFAULT_BANK_NAME).bankType(DEFAULT_BANK_TYPE);
        return bank;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createUpdatedEntity(EntityManager em) {
        Bank bank = new Bank().bankName(UPDATED_BANK_NAME).bankType(UPDATED_BANK_TYPE);
        return bank;
    }

    @BeforeEach
    public void initTest() {
        bank = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBank != null) {
            bankRepository.delete(insertedBank);
            insertedBank = null;
        }
    }

    @Test
    @Transactional
    void createBank() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);
        var returnedBankDTO = om.readValue(
            restBankMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BankDTO.class
        );

        // Validate the Bank in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBank = bankMapper.toEntity(returnedBankDTO);
        assertBankUpdatableFieldsEquals(returnedBank, getPersistedBank(returnedBank));

        insertedBank = returnedBank;
    }

    @Test
    @Transactional
    void createBankWithExistingId() throws Exception {
        // Create the Bank with an existing ID
        bank.setId(1L);
        BankDTO bankDTO = bankMapper.toDto(bank);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBankNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bank.setBankName(null);

        // Create the Bank, which fails.
        BankDTO bankDTO = bankMapper.toDto(bank);

        restBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBankTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bank.setBankType(null);

        // Create the Bank, which fails.
        BankDTO bankDTO = bankMapper.toDto(bank);

        restBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBanks() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        // Get all the bankList
        restBankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].bankType").value(hasItem(DEFAULT_BANK_TYPE.toString())));
    }

    @Test
    @Transactional
    void getBank() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        // Get the bank
        restBankMockMvc
            .perform(get(ENTITY_API_URL_ID, bank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bank.getId().intValue()))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.bankType").value(DEFAULT_BANK_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBank() throws Exception {
        // Get the bank
        restBankMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBank() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bank
        Bank updatedBank = bankRepository.findById(bank.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBank are not directly saved in db
        em.detach(updatedBank);
        updatedBank.bankName(UPDATED_BANK_NAME).bankType(UPDATED_BANK_TYPE);
        BankDTO bankDTO = bankMapper.toDto(updatedBank);

        restBankMockMvc
            .perform(put(ENTITY_API_URL_ID, bankDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isOk());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBankToMatchAllProperties(updatedBank);
    }

    @Test
    @Transactional
    void putNonExistingBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(put(ENTITY_API_URL_ID, bankDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBankWithPatch() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bank using partial update
        Bank partialUpdatedBank = new Bank();
        partialUpdatedBank.setId(bank.getId());

        restBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBank.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBank))
            )
            .andExpect(status().isOk());

        // Validate the Bank in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBank, bank), getPersistedBank(bank));
    }

    @Test
    @Transactional
    void fullUpdateBankWithPatch() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bank using partial update
        Bank partialUpdatedBank = new Bank();
        partialUpdatedBank.setId(bank.getId());

        partialUpdatedBank.bankName(UPDATED_BANK_NAME).bankType(UPDATED_BANK_TYPE);

        restBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBank.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBank))
            )
            .andExpect(status().isOk());

        // Validate the Bank in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankUpdatableFieldsEquals(partialUpdatedBank, getPersistedBank(partialUpdatedBank));
    }

    @Test
    @Transactional
    void patchNonExistingBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBank() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bank
        restBankMockMvc
            .perform(delete(ENTITY_API_URL_ID, bank.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bankRepository.count();
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

    protected Bank getPersistedBank(Bank bank) {
        return bankRepository.findById(bank.getId()).orElseThrow();
    }

    protected void assertPersistedBankToMatchAllProperties(Bank expectedBank) {
        assertBankAllPropertiesEquals(expectedBank, getPersistedBank(expectedBank));
    }

    protected void assertPersistedBankToMatchUpdatableProperties(Bank expectedBank) {
        assertBankAllUpdatablePropertiesEquals(expectedBank, getPersistedBank(expectedBank));
    }
}
