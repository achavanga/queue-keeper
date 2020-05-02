package za.co.covidify.model.business;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import za.co.covidify.model.contact.Address;
import za.co.covidify.model.user.Person;

@Entity
@Cacheable
@Table(name = "COMPANY")
public class Company extends PanacheEntity {

  @Column(name = "COMPANY_NAME", length = 100)
  public String companyName;

  @Column(name = "COMPANY_WEBSITE_URL", length = 200)
  public String websiteUrl;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "COMPANY_EMPLOYEE", joinColumns = @JoinColumn(name = "PERSON_ID"), inverseJoinColumns = @JoinColumn(name = "COMPANY_ID"))
  public Set<Person> companyEmployee = new HashSet<>();

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "COMPANY_ADDRESS", joinColumns = @JoinColumn(name = "ADDRESS_ID"), inverseJoinColumns = @JoinColumn(name = "COMPANY_ID"))
  public Set<Address> addresses = new HashSet<>();

  @Column(name = "EMAIL_ADDRESS", length = 100)
  public String emailAddress;

  @Column(name = "CELLPHONE", nullable = false, length = 20)
  public String cellphoneNumber;

  @Column(name = "PHONE", length = 20)
  public String phone;

  @OneToOne(targetEntity = Person.class, cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "CONTACT_PERSON_ID")
  public Person contactPerson;

}
