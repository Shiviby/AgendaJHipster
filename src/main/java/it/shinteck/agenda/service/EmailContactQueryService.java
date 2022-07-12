package it.shinteck.agenda.service;

import it.shinteck.agenda.domain.*; // for static metamodels
import it.shinteck.agenda.domain.EmailContact;
import it.shinteck.agenda.repository.EmailContactRepository;
import it.shinteck.agenda.service.criteria.EmailContactCriteria;
import it.shinteck.agenda.service.dto.EmailContactDTO;
import it.shinteck.agenda.service.mapper.EmailContactMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EmailContact} entities in the database.
 * The main input is a {@link EmailContactCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmailContactDTO} or a {@link Page} of {@link EmailContactDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailContactQueryService extends QueryService<EmailContact> {

    private final Logger log = LoggerFactory.getLogger(EmailContactQueryService.class);

    private final EmailContactRepository emailContactRepository;

    private final EmailContactMapper emailContactMapper;

    public EmailContactQueryService(EmailContactRepository emailContactRepository, EmailContactMapper emailContactMapper) {
        this.emailContactRepository = emailContactRepository;
        this.emailContactMapper = emailContactMapper;
    }

    /**
     * Return a {@link List} of {@link EmailContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmailContactDTO> findByCriteria(EmailContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmailContact> specification = createSpecification(criteria);
        return emailContactMapper.toDto(emailContactRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmailContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailContactDTO> findByCriteria(EmailContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailContact> specification = createSpecification(criteria);
        return emailContactRepository.findAll(specification, page).map(emailContactMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailContactCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmailContact> specification = createSpecification(criteria);
        return emailContactRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailContactCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailContact> createSpecification(EmailContactCriteria criteria) {
        Specification<EmailContact> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmailContact_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), EmailContact_.type));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EmailContact_.email));
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactId(),
                            root -> root.join(EmailContact_.contact, JoinType.LEFT).get(Contact_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
