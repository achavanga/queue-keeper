package za.co.covidify.model;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Cacheable
@Table(name = "SYS_USER")
public class User extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "userSequence", sequenceName = "user_id_seq", allocationSize = 1, initialValue = 10)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequence")
  public Long id;

  @Column(name = "USER_NAME", nullable = false, length = 60, unique = true)
  public String username;

  @NotNull
  @Size(min = 3, max = 150)
  @Column(name = "PASSWORD", length = 150, nullable = false)
  public String password;

  @Column(name = "LAST_SIGNED_IN")
  @JsonbDateFormat("yyyy/MM/dd HH:mm")
  public LocalDateTime lastSignedIn = LocalDateTime.now();

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_TYPE", nullable = false)
  public UserType type = UserType.GENERAL_USER;

  @OneToOne(targetEntity = Person.class, cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "PERSON_ID")
  public Person person;

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_STATUS", nullable = false)
  public UserStatus status = UserStatus.ACTIVE;

  @JsonbTransient
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
