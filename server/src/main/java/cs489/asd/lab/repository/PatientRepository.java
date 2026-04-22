package cs489.asd.lab.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import cs489.asd.lab.model.Patient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class PatientRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Patient> findAllByOrderByLastNameAscFirstNameAscPatientIdAsc() {
        return entityManager.createQuery(
                        "select p from Patient p join fetch p.user u order by u.lastName asc, u.firstName asc, p.userId asc",
                        Patient.class)
                .getResultList();
    }

    public Optional<Patient> findById(long patientId) {
        return Optional.ofNullable(entityManager.find(Patient.class, patientId));
    }

    public List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrContactPhoneContainingIgnoreCaseOrMailingAddressContainingIgnoreCaseOrderByLastNameAscFirstNameAscPatientIdAsc(
            String firstName,
            String lastName,
            String email,
            String contactPhone,
            String mailingAddress
    ) {
        return entityManager.createQuery(
                        "select p from Patient p join p.user u " +
                                "where lower(u.firstName) like lower(concat('%', :firstName, '%')) " +
                                "or lower(u.lastName) like lower(concat('%', :lastName, '%')) " +
                                "or lower(u.email) like lower(concat('%', :email, '%')) " +
                                "or lower(u.phoneNumber) like lower(concat('%', :contactPhone, '%')) " +
                                "or lower(p.mailingAddress) like lower(concat('%', :mailingAddress, '%')) " +
                                "order by u.lastName asc, u.firstName asc, p.userId asc",
                        Patient.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .setParameter("email", email)
                .setParameter("contactPhone", contactPhone)
                .setParameter("mailingAddress", mailingAddress)
                .getResultList();
    }

    @Transactional
    public Patient save(Patient patient) {
        if (patient.getUserId() == null) {
            entityManager.persist(patient);
            entityManager.flush();
            return patient;
        }

        return entityManager.merge(patient);
    }

    @Transactional
    public void delete(Patient patient) {
        Patient managed = entityManager.contains(patient) ? patient : entityManager.merge(patient);
        entityManager.remove(managed);
    }
}
