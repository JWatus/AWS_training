package training.aws.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import training.aws.dao.UserDao;
import training.aws.entity.User;

@Service
public class RDSService {

  private final UserDao userDao;

  public RDSService(UserDao userDao) {
    this.userDao = userDao;
  }

  @Transactional
  public User createUser(String userName, String userDescription) {
    if (userDao.findByUserName(userName).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists: " + userName);
    }
    return userDao.save(new User(userName, userDescription));
  }

  @Transactional(readOnly = true)
  public User getUser(String userName) {
    return userDao.findByUserName(userName)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userName));
  }

  @Transactional
  public User updateUser(String userName, String userDescription) {
    User user = userDao.findByUserName(userName)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userName));
    user.setUserDescription(userDescription);
    return userDao.update(user);
  }

  @Transactional
  public void deleteUser(String userName) {
    User user = userDao.findByUserName(userName)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userName));
    userDao.delete(user);
  }
}
