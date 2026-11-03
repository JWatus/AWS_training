package training.aws.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import training.aws.entity.User;

@Repository
public class UserDao {

  @PersistenceContext
  private EntityManager entityManager;

  public User save(User user) {
    entityManager.persist(user);
    return user;
  }

  public Optional<User> findByUserName(String userName) {
    try {
      User user = entityManager.createQuery(
              "SELECT u FROM User u WHERE u.userName = :userName", User.class)
          .setParameter("userName", userName)
          .getSingleResult();
      return Optional.of(user);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public User update(User user) {
    return entityManager.merge(user);
  }

  public void delete(User user) {
    entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
  }
}
