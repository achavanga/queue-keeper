package za.co.covidify.model.user;

import java.time.LocalDate;

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
import javax.persistence.Transient;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import za.co.covidify.model.person.Person;

@Entity
@Cacheable
@Table(name = "SYS_USER")
public class SysUser extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "userSequence", sequenceName = "user_id_seq", allocationSize = 1, initialValue = 2)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequence")
  public Long id;

  @Column(name = "USER_NAME", nullable = false, length = 60, unique = true)
  public String userName;

  @Column(name = "PASSWORD", length = 100)
  public String password;

  @Transient
  @JsonbTransient
  public Integer incorrectPasswordCounter;

  @Column(name = "LAST_SIGNED_IN")
  public LocalDate lastSignedIn;

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_TYPE", nullable = false)
  public UserType type = UserType.GENERAL_USER;

  @OneToOne(targetEntity = Person.class, cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "PERSON_ID")
  public Person person;

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_STATUS", nullable = false)
  public UserStatus status = UserStatus.ACTIVE;

}