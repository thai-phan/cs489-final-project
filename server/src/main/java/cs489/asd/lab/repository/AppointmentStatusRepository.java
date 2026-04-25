package cs489.asd.lab.repository;

import cs489.asd.lab.model.AppointmentStatus;
import cs489.asd.lab.model.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class AppointmentStatusRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<AppointmentStatus> findByName(String name) {
        return entityManager.createQuery(
                        "select s from AppointmentStatus s where upper(s.statusName) = upper(:name)",
                        AppointmentStatus.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }


    @Transactional
    public AppointmentStatus save(AppointmentStatus appointmentStatus) {
        entityManager.persist(appointmentStatus);
        return appointmentStatus;
    }

}
