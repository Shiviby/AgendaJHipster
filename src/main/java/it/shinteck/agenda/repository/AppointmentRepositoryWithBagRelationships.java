package it.shinteck.agenda.repository;

import it.shinteck.agenda.domain.Appointment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AppointmentRepositoryWithBagRelationships {
    Optional<Appointment> fetchBagRelationships(Optional<Appointment> appointment);

    List<Appointment> fetchBagRelationships(List<Appointment> appointments);

    Page<Appointment> fetchBagRelationships(Page<Appointment> appointments);
}
