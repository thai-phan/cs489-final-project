package cs489.asd.lab.repository;

import cs489.asd.lab.model.Dentist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class DentistRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Dentist> findById(long dentistId) {
        return Optional.ofNullable(entityManager.find(Dentist.class, dentistId));
    }

    public List<Dentist> findAllByOrderByLastNameAscFirstNameAscDentistIdAsc() {
        return entityManager.createQuery(
                        "select d from Dentist d join fetch d.user u order by u.lastName asc, u.firstName asc, d.userId asc",
                        Dentist.class)
                .getResultList();
    }

    public Optional<Dentist> findByUser_FullName(String fullName) {
        return entityManager.createQuery(
                        "select d from Dentist d join d.user u where concat(u.firstName, ' ', u.lastName) = :fullName",
                        Dentist.class)
                .setParameter("fullName", fullName)
                .getResultStream()
                .findFirst();
    }

    public Optional<Dentist> findByDentistIdNumber(String dentistIdNumber) {
        return entityManager.createQuery(
                        "select d from Dentist d join fetch d.user where lower(d.dentistIdNumber) = lower(:dentistIdNumber)",
                        Dentist.class)
                .setParameter("dentistIdNumber", dentistIdNumber)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    public Dentist save(Dentist dentist) {
        entityManager.persist(dentist);
        return dentist;
    }
}
