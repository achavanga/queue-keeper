package za.co.covidify.model.user;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import za.co.covidify.model.contact.Address;

@Entity
@Cacheable
@Table(name = "PERSON")
public class Person extends PanacheEntity {

  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  @Column(name = "MIDDLE_NAME", length = 100)
  private String middleName;

  @Column(name = "SURNAME", nullable = false, length = 100)
  private String surname;

  @Column(name = "INITIALS", length = 10)
  private String initials;

  @Column(name = "DISPLAY_NAME", nullable = false, length = 60)
  private String displayName;

  @Column(name = "EMAIL_ADDRESS", length = 100)
  private String emailAddress;

  @Column(name = "CELLPHONE", nullable = false, length = 20)
  private String cellphoneNumber;

  @Column(name = "ALT_CELLPHONE", length = 20)
  private String alternateCellPhone;

  @Column(name = "DATE_OF_BIRTH")
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDate dateOfBirth;

  @Column(name = "ID_NUMBER", length = 50)
  private String idNumber;

  @Column(name = "ID_TYPE", length = 50)
  private String idType;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "PERSON_ADDRESS", joinColumns = @JoinColumn(name = "ADDRESS_ID"), inverseJoinColumns = @JoinColumn(name = "PERSON_ID"))
  private Set<Address> addresses = new HashSet<>();

}
