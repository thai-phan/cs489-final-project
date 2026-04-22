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
                        "select d from Dentist d order by d.lastName asc, d.firstName asc, d.dentistId asc",
                        Dentist.class)
                .getResultList();
    }
}

