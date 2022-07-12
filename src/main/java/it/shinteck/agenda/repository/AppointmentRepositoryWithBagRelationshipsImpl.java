package it.shinteck.agenda.repository;

import it.shinteck.agenda.domain.Appointment;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AppointmentRepositoryWithBagRelationshipsImpl implements AppointmentRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Appointment> fetchBagRelationships(Optional<Appointment> appointment) {
        return appointment.map(this::fetchContacts);
    }

    @Override
    public Page<Appointment> fetchBagRelationships(Page<Appointment> appointments) {
        return new PageImpl<>(
            fetchBagRelationships(appointments.getContent()),
            appointments.getPageable(),
            appointments.getTotalElements()
        );
    }

    @Override
    public List<Appointment> fetchBagRelationships(List<Appointment> appointments) {
        return Optional.of(appointments).map(this::fetchContacts).orElse(Collections.emptyList());
    }

    Appointment fetchContacts(Appointment result) {
        return entityManager
            .createQuery(
                "select appointment from Appointment appointment left join fetch appointment.contacts where appointment is :appointment",
                Appointment.class
            )
            .setParameter("appointment", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Appointment> fetchContacts(List<Appointment> appointments) {
        return entityManager
            .createQuery(
                "select distinct appointment from Appointment appointment left join fetch appointment.contacts where appointment in :appointments",
                Appointment.class
            )
            .setParameter("appointments", appointments)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
