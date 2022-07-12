package it.shinteck.agenda.service;

import it.shinteck.agenda.domain.*; // for static metamodels
import it.shinteck.agenda.domain.PhoneContact;
import it.shinteck.agenda.repository.PhoneContactRepository;
import it.shinteck.agenda.service.criteria.PhoneContactCriteria;
import it.shinteck.agenda.service.dto.PhoneContactDTO;
import it.shinteck.agenda.service.mapper.PhoneContactMapper;
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
 * Service for executing complex queries for {@link PhoneContact} entities in the database.
 * The main input is a {@link PhoneContactCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PhoneContactDTO} or a {@link Page} of {@link PhoneContactDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PhoneContactQueryService extends QueryService<PhoneContact> {

    private final Logger log = LoggerFactory.getLogger(PhoneContactQueryService.class);

    private final PhoneContactRepository phoneContactRepository;

    private final PhoneContactMapper phoneContactMapper;

    public PhoneContactQueryService(PhoneContactRepository phoneContactRepository, PhoneContactMapper phoneContactMapper) {
        this.phoneContactRepository = phoneContactRepository;
        this.phoneContactMapper = phoneContactMapper;
    }

    /**
     * Return a {@link List} of {@link PhoneContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PhoneContactDTO> findByCriteria(PhoneContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PhoneContact> specification = createSpecification(criteria);
        return phoneContactMapper.toDto(phoneContactRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PhoneContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PhoneContactDTO> findByCriteria(PhoneContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PhoneContact> specification = createSpecification(criteria);
        return phoneContactRepository.findAll(specification, page).map(phoneContactMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PhoneContactCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PhoneContact> specification = createSpecification(criteria);
        return phoneContactRepository.count(specification);
    }

    /**
     * Function to convert {@link PhoneContactCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PhoneContact> createSpecification(PhoneContactCriteria criteria) {
        Specification<PhoneContact> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PhoneContact_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), PhoneContact_.type));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), PhoneContact_.phone));
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactId(),
                            root -> root.join(PhoneContact_.contact, JoinType.LEFT).get(Contact_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
