package it.shinteck.agenda.repository;

import it.shinteck.agenda.domain.PhoneContact;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PhoneContact entity.
 */
@Repository
public interface PhoneContactRepository extends JpaRepository<PhoneContact, Long>, JpaSpecificationExecutor<PhoneContact> {
    default Optional<PhoneContact> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PhoneContact> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PhoneContact> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct phoneContact from PhoneContact phoneContact left join fetch phoneContact.contact",
        countQuery = "select count(distinct phoneContact) from PhoneContact phoneContact"
    )
    Page<PhoneContact> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct phoneContact from PhoneContact phoneContact left join fetch phoneContact.contact")
    List<PhoneContact> findAllWithToOneRelationships();

    @Query("select phoneContact from PhoneContact phoneContact left join fetch phoneContact.contact where phoneContact.id =:id")
    Optional<PhoneContact> findOneWithToOneRelationships(@Param("id") Long id);
}
