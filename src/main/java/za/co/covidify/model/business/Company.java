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

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import za.co.covidify.model.contact.Address;
import za.co.covidify.model.user.Person;

@Entity
@Cacheable
public class Company extends PanacheEntity {

  @Column(name = "COMPANY_NAME", length = 100)
  private String companyName;

  @Column(name = "COMPANY_WEBSITE_URL", length = 200)
  private String websiteUrl;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "COMPANY_EMPLOYEE", joinColumns = @JoinColumn(name = "PERSON_ID"), inverseJoinColumns = @JoinColumn(name = "COMPANY_ID"))
  private Set<Person> companyEmployee = new HashSet<>();

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "COMPANY_ADDRESS", joinColumns = @JoinColumn(name = "ADDRESS_ID"), inverseJoinColumns = @JoinColumn(name = "COMPANY_ID"))
  private Set<Address> addresses = new HashSet<>();

}
