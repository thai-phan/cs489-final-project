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
                        "select p from Patient p order by p.lastName asc, p.firstName asc, p.patientId asc",
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
                        "select p from Patient p left join p.primaryAddress a " +
                                "where lower(p.firstName) like lower(concat('%', :firstName, '%')) " +
                                "or lower(p.lastName) like lower(concat('%', :lastName, '%')) " +
                                "or lower(p.email) like lower(concat('%', :email, '%')) " +
                                "or lower(p.contactPhone) like lower(concat('%', :contactPhone, '%')) " +
                                "or lower(a.mailingAddress) like lower(concat('%', :mailingAddress, '%')) " +
                                "or lower(a.city) like lower(concat('%', :mailingAddress, '%')) " +
                                "order by p.lastName asc, p.firstName asc, p.patientId asc",
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
        if (patient.getPatientId() == null) {
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

