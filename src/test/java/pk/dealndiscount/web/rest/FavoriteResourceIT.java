package pk.dealndiscount.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pk.dealndiscount.domain.FavoriteAsserts.*;
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
import pk.dealndiscount.domain.Favorite;
import pk.dealndiscount.domain.enumeration.FavoriteType;
import pk.dealndiscount.repository.FavoriteRepository;
import pk.dealndiscount.service.dto.FavoriteDTO;
import pk.dealndiscount.service.mapper.FavoriteMapper;

/**
 * Integration tests for the {@link FavoriteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FavoriteResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STORE_ID = "AAAAAAAAAA";
    private static final String UPDATED_STORE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_CITY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_ID = "AAAAAAAAAA";
    private static final String UPDATED_CARD_ID = "BBBBBBBBBB";

    private static final FavoriteType DEFAULT_FAVORITE_TYPE = FavoriteType.STORE;
    private static final FavoriteType UPDATED_FAVORITE_TYPE = FavoriteType.CITY;

    private static final String ENTITY_API_URL = "/api/favorites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFavoriteMockMvc;

    private Favorite favorite;

    private Favorite insertedFavorite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favorite createEntity(EntityManager em) {
        Favorite favorite = new Favorite()
            .userId(DEFAULT_USER_ID)
            .storeId(DEFAULT_STORE_ID)
            .cityId(DEFAULT_CITY_ID)
            .cardId(DEFAULT_CARD_ID)
            .favoriteType(DEFAULT_FAVORITE_TYPE);
        return favorite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favorite createUpdatedEntity(EntityManager em) {
        Favorite favorite = new Favorite()
            .userId(UPDATED_USER_ID)
            .storeId(UPDATED_STORE_ID)
            .cityId(UPDATED_CITY_ID)
            .cardId(UPDATED_CARD_ID)
            .favoriteType(UPDATED_FAVORITE_TYPE);
        return favorite;
    }

    @BeforeEach
    public void initTest() {
        favorite = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFavorite != null) {
            favoriteRepository.delete(insertedFavorite);
            insertedFavorite = null;
        }
    }

    @Test
    @Transactional
    void createFavorite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Favorite
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);
        var returnedFavoriteDTO = om.readValue(
            restFavoriteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FavoriteDTO.class
        );

        // Validate the Favorite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFavorite = favoriteMapper.toEntity(returnedFavoriteDTO);
        assertFavoriteUpdatableFieldsEquals(returnedFavorite, getPersistedFavorite(returnedFavorite));

        insertedFavorite = returnedFavorite;
    }

    @Test
    @Transactional
    void createFavoriteWithExistingId() throws Exception {
        // Create the Favorite with an existing ID
        favorite.setId(1L);
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoriteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Favorite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        favorite.setUserId(null);

        // Create the Favorite, which fails.
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

        restFavoriteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFavoriteTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        favorite.setFavoriteType(null);

        // Create the Favorite, which fails.
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

        restFavoriteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFavorites() throws Exception {
        // Initialize the database
        insertedFavorite = favoriteRepository.saveAndFlush(favorite);

        // Get all the favoriteList
        restFavoriteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favorite.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].storeId").value(hasItem(DEFAULT_STORE_ID)))
            .andExpect(jsonPath("$.[*].cityId").value(hasItem(DEFAULT_CITY_ID)))
            .andExpect(jsonPath("$.[*].cardId").value(hasItem(DEFAULT_CARD_ID)))
            .andExpect(jsonPath("$.[*].favoriteType").value(hasItem(DEFAULT_FAVORITE_TYPE.toString())));
    }

    @Test
    @Transactional
    void getFavorite() throws Exception {
        // Initialize the database
        insertedFavorite = favoriteRepository.saveAndFlush(favorite);

        // Get the favorite
        restFavoriteMockMvc
            .perform(get(ENTITY_API_URL_ID, favorite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(favorite.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.storeId").value(DEFAULT_STORE_ID))
            .andExpect(jsonPath("$.cityId").value(DEFAULT_CITY_ID))
            .andExpect(jsonPath("$.cardId").value(DEFAULT_CARD_ID))
            .andExpect(jsonPath("$.favoriteType").value(DEFAULT_FAVORITE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFavorite() throws Exception {
        // Get the favorite
        restFavoriteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFavorite() throws Exception {
        // Initialize the database
        insertedFavorite = favoriteRepository.saveAndFlush(favorite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the favorite
        Favorite updatedFavorite = favoriteRepository.findById(favorite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFavorite are not directly saved in db
        em.detach(updatedFavorite);
        updatedFavorite
            .userId(UPDATED_USER_ID)
            .storeId(UPDATED_STORE_ID)
            .cityId(UPDATED_CITY_ID)
            .cardId(UPDATED_CARD_ID)
            .favoriteType(UPDATED_FAVORITE_TYPE);
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(updatedFavorite);

        restFavoriteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favoriteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(favoriteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Favorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFavoriteToMatchAllProperties(updatedFavorite);
    }

    @Test
    @Transactional
    void putNonExistingFavorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favorite.setId(longCount.incrementAndGet());

        // Create the Favorite
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoriteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favoriteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(favoriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFavorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favorite.setId(longCount.incrementAndGet());

        // Create the Favorite
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(favoriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFavorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favorite.setId(longCount.incrementAndGet());

        // Create the Favorite
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Favorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFavoriteWithPatch() throws Exception {
        // Initialize the database
        insertedFavorite = favoriteRepository.saveAndFlush(favorite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the favorite using partial update
        Favorite partialUpdatedFavorite = new Favorite();
        partialUpdatedFavorite.setId(favorite.getId());

        partialUpdatedFavorite.userId(UPDATED_USER_ID).storeId(UPDATED_STORE_ID);

        restFavoriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavorite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFavorite))
            )
            .andExpect(status().isOk());

        // Validate the Favorite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFavoriteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFavorite, favorite), getPersistedFavorite(favorite));
    }

    @Test
    @Transactional
    void fullUpdateFavoriteWithPatch() throws Exception {
        // Initialize the database
        insertedFavorite = favoriteRepository.saveAndFlush(favorite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the favorite using partial update
        Favorite partialUpdatedFavorite = new Favorite();
        partialUpdatedFavorite.setId(favorite.getId());

        partialUpdatedFavorite
            .userId(UPDATED_USER_ID)
            .storeId(UPDATED_STORE_ID)
            .cityId(UPDATED_CITY_ID)
            .cardId(UPDATED_CARD_ID)
            .favoriteType(UPDATED_FAVORITE_TYPE);

        restFavoriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavorite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFavorite))
            )
            .andExpect(status().isOk());

        // Validate the Favorite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFavoriteUpdatableFieldsEquals(partialUpdatedFavorite, getPersistedFavorite(partialUpdatedFavorite));
    }

    @Test
    @Transactional
    void patchNonExistingFavorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favorite.setId(longCount.incrementAndGet());

        // Create the Favorite
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, favoriteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(favoriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFavorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favorite.setId(longCount.incrementAndGet());

        // Create the Favorite
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(favoriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFavorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favorite.setId(longCount.incrementAndGet());

        // Create the Favorite
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(favoriteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Favorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFavorite() throws Exception {
        // Initialize the database
        insertedFavorite = favoriteRepository.saveAndFlush(favorite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the favorite
        restFavoriteMockMvc
            .perform(delete(ENTITY_API_URL_ID, favorite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return favoriteRepository.count();
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

    protected Favorite getPersistedFavorite(Favorite favorite) {
        return favoriteRepository.findById(favorite.getId()).orElseThrow();
    }

    protected void assertPersistedFavoriteToMatchAllProperties(Favorite expectedFavorite) {
        assertFavoriteAllPropertiesEquals(expectedFavorite, getPersistedFavorite(expectedFavorite));
    }

    protected void assertPersistedFavoriteToMatchUpdatableProperties(Favorite expectedFavorite) {
        assertFavoriteAllUpdatablePropertiesEquals(expectedFavorite, getPersistedFavorite(expectedFavorite));
    }
}
