package za.co.covidify.model.user;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class User extends PanacheEntity {

  @Column(name = "USER_NAME", nullable = false, length = 60, unique = true)
  private String userName;

  @Column(name = "PASSWORD", length = 100)
  private String password;

  @Transient
  private Integer incorrectPasswordCounter;

  @Column(name = "LAST_SIGNED_IN")
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDate lastSignedIn;

  @Column(name = "CHANGE_PASSWORD", nullable = false)
  private Boolean changePassword = false;

  @Enumerated(EnumType.STRING)
  @Column(name = "USER_TYPE", nullable = false)
  private UserType type = UserType.GENERAL_USER;

}
