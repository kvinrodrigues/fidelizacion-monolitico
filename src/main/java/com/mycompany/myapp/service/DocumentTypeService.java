package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.DocumentType;
import com.mycompany.myapp.repository.DocumentTypeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentType}.
 */
@Service
@Transactional
public class DocumentTypeService {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeService.class);

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeService(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    /**
     * Save a documentType.
     *
     * @param documentType the entity to save.
     * @return the persisted entity.
     */
    public DocumentType save(DocumentType documentType) {
        log.debug("Request to save DocumentType : {}", documentType);
        return documentTypeRepository.save(documentType);
    }

    /**
     * Partially update a documentType.
     *
     * @param documentType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentType> partialUpdate(DocumentType documentType) {
        log.debug("Request to partially update DocumentType : {}", documentType);

        return documentTypeRepository
            .findById(documentType.getId())
            .map(existingDocumentType -> {
                if (documentType.getName() != null) {
                    existingDocumentType.setName(documentType.getName());
                }
                if (documentType.getDescription() != null) {
                    existingDocumentType.setDescription(documentType.getDescription());
                }

                return existingDocumentType;
            })
            .map(documentTypeRepository::save);
    }

    /**
     * Get all the documentTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentType> findAll() {
        log.debug("Request to get all DocumentTypes");
        return documentTypeRepository.findAll();
    }

    /**
     * Get one documentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentType> findOne(Long id) {
        log.debug("Request to get DocumentType : {}", id);
        return documentTypeRepository.findById(id);
    }

    /**
     * Delete the documentType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentType : {}", id);
        documentTypeRepository.deleteById(id);
    }
}
