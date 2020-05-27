package za.co.covidify.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

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

  @Email
  @Size(max = 200)
  @Column(name = "EMAIL_ADDRESS", length = 200, unique = true)
  public String emailAddress;

  @Column(name = "CELLPHONE", nullable = false, length = 20)
  public String cellphoneNumber;

  @Column(name = "ID_NUMBER", length = 50)
  public String idNumber;

  @Column(name = "DATE_CREATED")
  @JsonbDateFormat("yyyy/MM/dd HH:mm")
  public LocalDateTime dateCreated = LocalDateTime.now();

  @OneToOne(optional = false)
  @JoinColumn(name = "ADDRESS_ID")
  public Address address;

  @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonbTransient
  public Set<Queue> queue;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMPANY_ID")
  public Company company;

}
