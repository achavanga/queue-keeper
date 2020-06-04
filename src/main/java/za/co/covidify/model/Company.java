package za.co.covidify.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Cacheable
@Table(name = "COMPANY")
public class Company extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "companySequence", sequenceName = "company_id_seq", allocationSize = 1, initialValue = 10)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companySequence")
  public Long id;

  @Column(name = "COMPANY_NAME", nullable = false, length = 100)
  public String companyName;

  @Column(name = "COMPANY_WEBSITE_URL", length = 200)
  public String websiteUrl;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
  @LazyCollection(LazyCollectionOption.FALSE)
  @JsonbTransient
  public Set<Person> companyEmployees = new HashSet<>();

  @OneToOne(optional = false)
  @JoinColumn(name = "ADDRESS_ID")
  public Address address;

  @Column(name = "EMAIL_ADDRESS", length = 100)
  public String emailAddress;

  @Column(name = "CELLPHONE", nullable = false, length = 20)
  public String cellphoneNumber;

  @Column(name = "PHONE", length = 20)
  public String phone;

  @OneToOne(optional = false)
  @JoinColumn(name = "CONTACT_PERSON_ID")
  public Person contactPerson;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", fetch = FetchType.EAGER)
  @JsonbTransient
  public List<QueueHeader> queueHeader = new ArrayList<>();

  @Column(name = "DATE_CREATED")
  // @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  @JsonbTransient
  public LocalDateTime dateCreated = LocalDateTime.now();

  @Column(name = "IS_COMPANY_ACTIVE")
  @JsonbTransient
  public boolean isCompanyActive = true;

}
