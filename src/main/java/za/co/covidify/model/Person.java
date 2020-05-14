package za.co.covidify.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Cacheable
@Table(name = "PERSON")
public class Person extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "personSequence", sequenceName = "person_id_seq", allocationSize = 1, initialValue = 2)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSequence")
  public Long id;

  @Column(name = "NAME", length = 100)
  public String name;

  @Column(name = "SURNAME", length = 100)
  public String surname;

  @Column(name = "EMAIL_ADDRESS", length = 100)
  public String emailAddress;

  @Column(name = "CELLPHONE", nullable = false, length = 20)
  public String cellphoneNumber;

  @Column(name = "ID_NUMBER", length = 50)
  public String idNumber;

  @Column(name = "DATE_CREATED")
  @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  public LocalDate dateCreated;

  @OneToOne(optional = false)
  @JoinColumn(name = "ADDRESS_ID")
  public Address address;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "person", fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Queue> queue = new ArrayList<>();

}
