package training.aws.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String userName;

  private String userDescription;

  public User() {
  }

  public User(String userName, String userDescription) {
    this.userName = userName;
    this.userDescription = userDescription;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserDescription() {
    return userDescription;
  }

  public void setUserDescription(String userDescription) {
    this.userDescription = userDescription;
  }
}
