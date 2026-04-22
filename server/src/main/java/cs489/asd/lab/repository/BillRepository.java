package cs489.asd.lab.repository;

import cs489.asd.lab.model.Bill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class BillRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean existsUnpaidByPatientId(long patientId) {
        Long count = entityManager.createQuery(
                        "select count(b) from Bill b where b.patient.userId = :patientId and b.paid = false",
                        Long.class)
                .setParameter("patientId", patientId)
                .getSingleResult();
        return count != null && count > 0;
    }
}

