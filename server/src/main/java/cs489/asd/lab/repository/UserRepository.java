package cs489.asd.lab.repository;

import cs489.asd.lab.model.Role;
import cs489.asd.lab.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> findByUsername(String username) {
        return entityManager.createQuery(
                        "select u from User u left join fetch u.roles where lower(u.username) = lower(:username)",
                        User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    public Optional<Role> findRoleByName(String roleName) {
        return entityManager.createQuery(
                        "select r from Role r where upper(r.name) = upper(:roleName)",
                        Role.class)
                .setParameter("roleName", roleName)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }
}

