package cs489.asd.lab.repository;

import cs489.asd.lab.model.Appointment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AppointmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public long countDentistAppointmentsBetween(long dentistId, LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        return entityManager.createQuery(
                        "select count(a) from Appointment a " +
                                "where a.dentist.id = :dentistId " +
                                "and a.appointmentDateTime >= :fromInclusive " +
                                "and a.appointmentDateTime < :toExclusive " +
                                "and a.status.statusName <> :cancelled",
                        Long.class)
                .setParameter("dentistId", dentistId)
                .setParameter("fromInclusive", fromInclusive)
                .setParameter("toExclusive", toExclusive)
                .setParameter("cancelled", "CANCELLED")
                .getSingleResult();
    }

    public List<Appointment> findByDentistIdOrderByDateTimeAsc(long dentistId) {
        return entityManager.createQuery(
                        "select a from Appointment a " +
                                "join fetch a.patient p " +
                                "join fetch a.surgery s " +
                                "where a.dentist.id = :dentistId " +
                                "order by a.appointmentDateTime asc, a.appointmentId asc",
                        Appointment.class)
                .setParameter("dentistId", dentistId)
                .getResultList();
    }

    public List<Appointment> findBySurgeryLocationOrderByDateTimeAsc(String locationAddress) {
        return entityManager.createQuery(
                        "select a from Appointment a " +
                                "join fetch a.patient p " +
                                "join fetch a.surgery s " +
                                "where lower(s.locationAddress) = lower(:locationAddress) " +
                                "order by a.appointmentDateTime asc, a.appointmentId asc",
                        Appointment.class)
                .setParameter("locationAddress", locationAddress)
                .getResultList();
    }

    public List<Appointment> findByPatientIdAndDateOrderByDateTimeAsc(long patientId, LocalDate appointmentDate) {
        return entityManager.createQuery(
                        "select a from Appointment a " +
                                "join fetch a.dentist d " +
                                "join fetch a.patient p " +
                                "join fetch a.surgery s " +
                                "where a.patient.id = :patientId " +
                                "and a.appointmentDateTime >= :fromInclusive " +
                                "and a.appointmentDateTime < :toExclusive " +
                                "order by a.appointmentDateTime asc, a.appointmentId asc",
                        Appointment.class)
                .setParameter("patientId", patientId)
                .setParameter("fromInclusive", appointmentDate.atStartOfDay())
                .setParameter("toExclusive", appointmentDate.plusDays(1).atStartOfDay())
                .getResultList();
    }

    public List<Appointment> findByPatientIdOrderByDateTimeAsc(long patientId) {
        return entityManager.createQuery(
                        "select a from Appointment a " +
                                "join fetch a.dentist d " +
                                "join fetch a.patient p " +
                                "join fetch a.surgery s " +
                                "where a.patient.id = :patientId " +
                                "order by a.appointmentDateTime asc, a.appointmentId asc",
                        Appointment.class)
                .setParameter("patientId", patientId)
                .getResultList();
    }

    public List<Appointment> findPendingAppointmentsOrderByDateTimeAsc() {
        return entityManager.createQuery(
                        "select a from Appointment a " +
                                "join fetch a.dentist d " +
                                "join fetch a.patient p " +
                                "join fetch a.surgery s " +
                                "join fetch a.status st " +
                                "where a.status.statusName <> :completed " +
                                "and a.status.statusName <> :cancelled " +
                                "order by a.appointmentDateTime asc, a.appointmentId asc",
                        Appointment.class)
                .setParameter("completed", "COMPLETED")
                .setParameter("cancelled", "CANCELLED")
                .getResultList();
    }

    public List<Appointment> findScheduledAppointmentsByDentistIdOrderByDateTimeAsc(long dentistId) {
        return entityManager.createQuery(
                        "select a from Appointment a " +
                                "join fetch a.dentist d " +
                                "join fetch a.patient p " +
                                "join fetch a.surgery s " +
                                "join fetch a.status st " +
                                "where a.dentist.id = :dentistId " +
                                "and upper(a.status.statusName) = 'SCHEDULED' " +
                                "order by a.appointmentDateTime asc, a.appointmentId asc",
                        Appointment.class)
                .setParameter("dentistId", dentistId)
                .getResultList();
    }

    public java.util.Optional<Appointment> findByAppointmentId(long appointmentId) {
        return entityManager.createQuery(
                        "select a from Appointment a " +
                                "join fetch a.dentist d " +
                                "join fetch a.patient p " +
                                "join fetch a.surgery s " +
                                "join fetch a.status st " +
                                "where a.appointmentId = :appointmentId",
                        Appointment.class)
                .setParameter("appointmentId", appointmentId)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    public Appointment save(Appointment appointment) {
        if (appointment.getAppointmentId() == null) {
            entityManager.persist(appointment);
            entityManager.flush();
            return appointment;
        }

        return entityManager.merge(appointment);
    }
}
