package pk.dealndiscount.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.dealndiscount.domain.Card;
import pk.dealndiscount.repository.CardRepository;
import pk.dealndiscount.service.CardService;
import pk.dealndiscount.service.dto.CardDTO;
import pk.dealndiscount.service.mapper.CardMapper;

/**
 * Service Implementation for managing {@link pk.dealndiscount.domain.Card}.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {

    private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public CardDTO save(CardDTO cardDTO) {
        log.debug("Request to save Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    @Override
    public CardDTO update(CardDTO cardDTO) {
        log.debug("Request to update Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    @Override
    public Optional<CardDTO> partialUpdate(CardDTO cardDTO) {
        log.debug("Request to partially update Card : {}", cardDTO);

        return cardRepository
            .findById(cardDTO.getId())
            .map(existingCard -> {
                cardMapper.partialUpdate(existingCard, cardDTO);

                return existingCard;
            })
            .map(cardRepository::save)
            .map(cardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardDTO> findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        return cardRepository.findById(id).map(cardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }
}
