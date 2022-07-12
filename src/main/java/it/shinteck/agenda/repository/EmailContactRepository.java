package it.shinteck.agenda.repository;

import it.shinteck.agenda.domain.EmailContact;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EmailContact entity.
 */
@Repository
public interface EmailContactRepository extends JpaRepository<EmailContact, Long>, JpaSpecificationExecutor<EmailContact> {
    default Optional<EmailContact> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EmailContact> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EmailContact> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct emailContact from EmailContact emailContact left join fetch emailContact.contact",
        countQuery = "select count(distinct emailContact) from EmailContact emailContact"
    )
    Page<EmailContact> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct emailContact from EmailContact emailContact left join fetch emailContact.contact")
    List<EmailContact> findAllWithToOneRelationships();

    @Query("select emailContact from EmailContact emailContact left join fetch emailContact.contact where emailContact.id =:id")
    Optional<EmailContact> findOneWithToOneRelationships(@Param("id") Long id);
}
