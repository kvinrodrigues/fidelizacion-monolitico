package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Nacionality;
import com.mycompany.myapp.repository.NacionalityRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Nacionality}.
 */
@Service
@Transactional
public class NacionalityService {

    private final Logger log = LoggerFactory.getLogger(NacionalityService.class);

    private final NacionalityRepository nacionalityRepository;

    public NacionalityService(NacionalityRepository nacionalityRepository) {
        this.nacionalityRepository = nacionalityRepository;
    }

    /**
     * Save a nacionality.
     *
     * @param nacionality the entity to save.
     * @return the persisted entity.
     */
    public Nacionality save(Nacionality nacionality) {
        log.debug("Request to save Nacionality : {}", nacionality);
        return nacionalityRepository.save(nacionality);
    }

    /**
     * Partially update a nacionality.
     *
     * @param nacionality the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Nacionality> partialUpdate(Nacionality nacionality) {
        log.debug("Request to partially update Nacionality : {}", nacionality);

        return nacionalityRepository
            .findById(nacionality.getId())
            .map(existingNacionality -> {
                if (nacionality.getName() != null) {
                    existingNacionality.setName(nacionality.getName());
                }
                if (nacionality.getDescription() != null) {
                    existingNacionality.setDescription(nacionality.getDescription());
                }

                return existingNacionality;
            })
            .map(nacionalityRepository::save);
    }

    /**
     * Get all the nacionalities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Nacionality> findAll() {
        log.debug("Request to get all Nacionalities");
        return nacionalityRepository.findAll();
    }

    /**
     * Get one nacionality by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Nacionality> findOne(Long id) {
        log.debug("Request to get Nacionality : {}", id);
        return nacionalityRepository.findById(id);
    }

    /**
     * Delete the nacionality by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Nacionality : {}", id);
        nacionalityRepository.deleteById(id);
    }
}
