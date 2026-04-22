package cs489.asd.lab.repository;

import cs489.asd.lab.model.Surgery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class SurgeryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Surgery> findById(long surgeryId) {
        return Optional.ofNullable(entityManager.find(Surgery.class, surgeryId));
    }
}

