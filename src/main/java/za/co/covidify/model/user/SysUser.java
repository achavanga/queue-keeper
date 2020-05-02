package za.co.covidify.model.user;

import java.time.LocalDate;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Cacheable
@Table(name = "SYS_USER")
public class SysUser extends PanacheEntity {

  @Column(name = "USER_NAME", nullable = false, length = 60, unique = true)
  public String userName;

  @Column(name = "PASSWORD", length = 100)
  public String password;

  @Transient
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
  public Status status = Status.ACTIVE;

  public SysUser() {
  }

  public SysUser(String userName) {
    this.userName = userName;
  }

}
