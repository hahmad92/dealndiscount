package pk.dealndiscount.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.dealndiscount.domain.Favorite;
import pk.dealndiscount.repository.FavoriteRepository;
import pk.dealndiscount.service.FavoriteService;
import pk.dealndiscount.service.dto.FavoriteDTO;
import pk.dealndiscount.service.mapper.FavoriteMapper;

/**
 * Service Implementation for managing {@link pk.dealndiscount.domain.Favorite}.
 */
@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private static final Logger log = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    private final FavoriteRepository favoriteRepository;

    private final FavoriteMapper favoriteMapper;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public FavoriteDTO save(FavoriteDTO favoriteDTO) {
        log.debug("Request to save Favorite : {}", favoriteDTO);
        Favorite favorite = favoriteMapper.toEntity(favoriteDTO);
        favorite = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(favorite);
    }

    @Override
    public FavoriteDTO update(FavoriteDTO favoriteDTO) {
        log.debug("Request to update Favorite : {}", favoriteDTO);
        Favorite favorite = favoriteMapper.toEntity(favoriteDTO);
        favorite = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(favorite);
    }

    @Override
    public Optional<FavoriteDTO> partialUpdate(FavoriteDTO favoriteDTO) {
        log.debug("Request to partially update Favorite : {}", favoriteDTO);

        return favoriteRepository
            .findById(favoriteDTO.getId())
            .map(existingFavorite -> {
                favoriteMapper.partialUpdate(existingFavorite, favoriteDTO);

                return existingFavorite;
            })
            .map(favoriteRepository::save)
            .map(favoriteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FavoriteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Favorites");
        return favoriteRepository.findAll(pageable).map(favoriteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FavoriteDTO> findOne(Long id) {
        log.debug("Request to get Favorite : {}", id);
        return favoriteRepository.findById(id).map(favoriteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Favorite : {}", id);
        favoriteRepository.deleteById(id);
    }
}
