package za.co.covidify.model.person;

import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import za.co.covidify.model.contact.Address;

@Entity
@Cacheable
@Table(name = "PERSON")
public class Person extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "personSequence", sequenceName = "person_id_seq", allocationSize = 1, initialValue = 2)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSequence")
  public Long id;

  @Column(name = "NAME", nullable = false, length = 100)
  public String name;

  @Column(name = "SURNAME", nullable = false, length = 100)
  public String surname;

  @Column(name = "TITLE", length = 10)
  @Enumerated(EnumType.STRING)
  public Title title = Title.Other;

  @Column(name = "DISPLAY_NAME", nullable = false, length = 100)
  public String displayName;

  @Column(name = "EMAIL_ADDRESS", length = 100)
  public String emailAddress;

  @Column(name = "CELLPHONE", nullable = false, length = 20)
  public String cellphoneNumber;

  @Column(name = "ID_NUMBER", length = 50)
  public String idNumber;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "PERSON_ADDRESS", joinColumns = @JoinColumn(name = "ADDRESS_ID"), inverseJoinColumns = @JoinColumn(name = "PERSON_ID"))
  public Set<Address> addresses = new HashSet<>();

}
