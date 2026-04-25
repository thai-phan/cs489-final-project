package cs489.asd.lab.repository;

import cs489.asd.lab.model.Role;
import cs489.asd.lab.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public class RoleRepository {

    @PersistenceContext
    EntityManager entityManager;

    public Optional<Role> findByRoleName(String roleName) {
        return Optional.empty();
    }
    @Transactional
    public Role save(Role role) {
        entityManager.persist(role);
        return role;
    }
}
