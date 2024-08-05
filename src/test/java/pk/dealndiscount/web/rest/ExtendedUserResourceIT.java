package pk.dealndiscount.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pk.dealndiscount.domain.ExtendedUserAsserts.*;
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
import pk.dealndiscount.domain.ExtendedUser;
import pk.dealndiscount.repository.ExtendedUserRepository;

/**
 * Integration tests for the {@link ExtendedUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtendedUserResourceIT {

    private static final Instant DEFAULT_DOB = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DOB = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/extended-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExtendedUserRepository extendedUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtendedUserMockMvc;

    private ExtendedUser extendedUser;

    private ExtendedUser insertedExtendedUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtendedUser createEntity(EntityManager em) {
        ExtendedUser extendedUser = new ExtendedUser().dob(DEFAULT_DOB);
        return extendedUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtendedUser createUpdatedEntity(EntityManager em) {
        ExtendedUser extendedUser = new ExtendedUser().dob(UPDATED_DOB);
        return extendedUser;
    }

    @BeforeEach
    public void initTest() {
        extendedUser = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedExtendedUser != null) {
            extendedUserRepository.delete(insertedExtendedUser);
            insertedExtendedUser = null;
        }
    }

    @Test
    @Transactional
    void createExtendedUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExtendedUser
        var returnedExtendedUser = om.readValue(
            restExtendedUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extendedUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExtendedUser.class
        );

        // Validate the ExtendedUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExtendedUserUpdatableFieldsEquals(returnedExtendedUser, getPersistedExtendedUser(returnedExtendedUser));

        insertedExtendedUser = returnedExtendedUser;
    }

    @Test
    @Transactional
    void createExtendedUserWithExistingId() throws Exception {
        // Create the ExtendedUser with an existing ID
        extendedUser.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtendedUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extendedUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExtendedUsers() throws Exception {
        // Initialize the database
        insertedExtendedUser = extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList
        restExtendedUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendedUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())));
    }

    @Test
    @Transactional
    void getExtendedUser() throws Exception {
        // Initialize the database
        insertedExtendedUser = extendedUserRepository.saveAndFlush(extendedUser);

        // Get the extendedUser
        restExtendedUserMockMvc
            .perform(get(ENTITY_API_URL_ID, extendedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extendedUser.getId().intValue()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExtendedUser() throws Exception {
        // Get the extendedUser
        restExtendedUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExtendedUser() throws Exception {
        // Initialize the database
        insertedExtendedUser = extendedUserRepository.saveAndFlush(extendedUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extendedUser
        ExtendedUser updatedExtendedUser = extendedUserRepository.findById(extendedUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExtendedUser are not directly saved in db
        em.detach(updatedExtendedUser);
        updatedExtendedUser.dob(UPDATED_DOB);

        restExtendedUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExtendedUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExtendedUser))
            )
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExtendedUserToMatchAllProperties(updatedExtendedUser);
    }

    @Test
    @Transactional
    void putNonExistingExtendedUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extendedUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extendedUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extendedUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtendedUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extendedUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extendedUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtendedUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extendedUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extendedUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtendedUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtendedUserWithPatch() throws Exception {
        // Initialize the database
        insertedExtendedUser = extendedUserRepository.saveAndFlush(extendedUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extendedUser using partial update
        ExtendedUser partialUpdatedExtendedUser = new ExtendedUser();
        partialUpdatedExtendedUser.setId(extendedUser.getId());

        partialUpdatedExtendedUser.dob(UPDATED_DOB);

        restExtendedUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtendedUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtendedUser))
            )
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtendedUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExtendedUser, extendedUser),
            getPersistedExtendedUser(extendedUser)
        );
    }

    @Test
    @Transactional
    void fullUpdateExtendedUserWithPatch() throws Exception {
        // Initialize the database
        insertedExtendedUser = extendedUserRepository.saveAndFlush(extendedUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extendedUser using partial update
        ExtendedUser partialUpdatedExtendedUser = new ExtendedUser();
        partialUpdatedExtendedUser.setId(extendedUser.getId());

        partialUpdatedExtendedUser.dob(UPDATED_DOB);

        restExtendedUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtendedUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtendedUser))
            )
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtendedUserUpdatableFieldsEquals(partialUpdatedExtendedUser, getPersistedExtendedUser(partialUpdatedExtendedUser));
    }

    @Test
    @Transactional
    void patchNonExistingExtendedUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extendedUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extendedUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extendedUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtendedUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extendedUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extendedUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtendedUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extendedUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(extendedUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtendedUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtendedUser() throws Exception {
        // Initialize the database
        insertedExtendedUser = extendedUserRepository.saveAndFlush(extendedUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the extendedUser
        restExtendedUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, extendedUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return extendedUserRepository.count();
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

    protected ExtendedUser getPersistedExtendedUser(ExtendedUser extendedUser) {
        return extendedUserRepository.findById(extendedUser.getId()).orElseThrow();
    }

    protected void assertPersistedExtendedUserToMatchAllProperties(ExtendedUser expectedExtendedUser) {
        assertExtendedUserAllPropertiesEquals(expectedExtendedUser, getPersistedExtendedUser(expectedExtendedUser));
    }

    protected void assertPersistedExtendedUserToMatchUpdatableProperties(ExtendedUser expectedExtendedUser) {
        assertExtendedUserAllUpdatablePropertiesEquals(expectedExtendedUser, getPersistedExtendedUser(expectedExtendedUser));
    }
}
