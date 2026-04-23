package cs489.asd.lab.repository;

import cs489.asd.lab.model.Manager;
import cs489.asd.lab.model.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class ManagerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Manager> findById(long managerId) {
        return Optional.ofNullable(entityManager.find(Manager.class, managerId));
    }

    public List<Manager> findAllByOrderByLastNameAscFirstNameAscManagerIdAsc() {
        return entityManager.createQuery(
                        "select m from Manager m join fetch m.user u order by u.lastName asc, u.firstName asc, m.userId asc",
                        Manager.class)
                .getResultList();
    }

    @Transactional
    public Manager save(Manager manager) {
        if (manager.getUserId() == null) {
            entityManager.persist(manager);
            entityManager.flush();
            return manager;
        }

        return entityManager.merge(manager);
    }
}
