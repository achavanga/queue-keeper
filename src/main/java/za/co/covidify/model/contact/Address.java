package za.co.covidify.model.contact;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Cacheable
@Table(name = "ADDRESS")
public class Address extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "addressSequence", sequenceName = "address_id_seq", allocationSize = 1, initialValue = 2)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressSequence")
  public Long id;

  @Column(name = "ADDRESS_NAME", length = 100)
  public String name;

  @Column(name = "UNIT_NUMBER", length = 60)
  public String unitNumber;

  @Column(name = "COMPLEX")
  public String complex;

  @Column(name = "STREET", length = 60)
  public String street;

  @Column(name = "SUBURB", length = 60)
  public String suburb;

  @Column(name = "CITY", length = 60)
  public String city;

  @Column(name = "PROVINCE", length = 60)
  public String province;

  @Column(name = "POSTAL_CODE", length = 10)
  public String postalCode;

  @Column(name = "COUNTRY", length = 60)
  public String country;

  @Column(name = "STREET_NUMBER")
  public String streetNumber;

  @Column(name = "STREET_NAME", length = 60)
  public String streetName;

  @Enumerated(EnumType.STRING)
  @Column(name = "ADDRESS_TYPE", nullable = false)
  public AddressType type;

  @Column(name = "POSTAL_SAME_AS_PYSICAL")
  public Boolean postalSameAsPhysical = false;

  @Column(name = "WORK_SAME_AS_PYSICAL")
  public Boolean workSameAsPhysical = false;

  public Address() {
  }

}
