package za.co.covidify.model.contact;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Cacheable
public class Address extends PanacheEntity {

  @Column(name = "ADDRESS_NAME", length = 100)
  private String name;

  @Column(name = "UNIT_NUMBER", length = 60)
  private String unitNumber;

  @Column(name = "COMPLEX")
  private String complex;

  @Column(name = "STREET", length = 60)
  private String street;

  @Column(name = "SUBURB", length = 60)
  private String suburb;

  @Column(name = "CITY", length = 60)
  private String city;

  @Column(name = "PROVINCE", length = 60)
  private String province;

  @Column(name = "POSTAL_CODE", length = 10)
  private String postalCode;

  @Column(name = "COUNTRY", length = 60)
  private String country;

  @Column(name = "STREET_NUMBER")
  private String streetNumber;

  @Column(name = "STREET_NAME", length = 60)
  private String streetName;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "ADDRESS_TYPE", nullable = false)
  private AddressType type;

  @Column(name = "POSTAL_SAME_AS_PYSICAL")
  private Boolean postalSameAsPhysical = false;

  @Column(name = "WORK_SAME_AS_PYSICAL")
  private Boolean workSameAsPhysical = false;

}
