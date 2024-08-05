package pk.dealndiscount.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.dealndiscount.domain.ExtendedUser;
import pk.dealndiscount.repository.ExtendedUserRepository;
import pk.dealndiscount.service.ExtendedUserService;

/**
 * Service Implementation for managing {@link pk.dealndiscount.domain.ExtendedUser}.
 */
@Service
@Transactional
public class ExtendedUserServiceImpl implements ExtendedUserService {

    private static final Logger log = LoggerFactory.getLogger(ExtendedUserServiceImpl.class);

    private final ExtendedUserRepository extendedUserRepository;

    public ExtendedUserServiceImpl(ExtendedUserRepository extendedUserRepository) {
        this.extendedUserRepository = extendedUserRepository;
    }

    @Override
    public ExtendedUser save(ExtendedUser extendedUser) {
        log.debug("Request to save ExtendedUser : {}", extendedUser);
        return extendedUserRepository.save(extendedUser);
    }

    @Override
    public ExtendedUser update(ExtendedUser extendedUser) {
        log.debug("Request to update ExtendedUser : {}", extendedUser);
        return extendedUserRepository.save(extendedUser);
    }

    @Override
    public Optional<ExtendedUser> partialUpdate(ExtendedUser extendedUser) {
        log.debug("Request to partially update ExtendedUser : {}", extendedUser);

        return extendedUserRepository
            .findById(extendedUser.getId())
            .map(existingExtendedUser -> {
                if (extendedUser.getDob() != null) {
                    existingExtendedUser.setDob(extendedUser.getDob());
                }

                return existingExtendedUser;
            })
            .map(extendedUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExtendedUser> findAll() {
        log.debug("Request to get all ExtendedUsers");
        return extendedUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExtendedUser> findOne(Long id) {
        log.debug("Request to get ExtendedUser : {}", id);
        return extendedUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExtendedUser : {}", id);
        extendedUserRepository.deleteById(id);
    }
}
