package za.co.covidify.model;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
  @SequenceGenerator(name = "addressSequence", sequenceName = "address_id_seq", allocationSize = 1, initialValue = 10)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressSequence")
  public Long id;

  @Column(name = "ADDRESS_LINE1", nullable = false, length = 60)
  public String addressLine;

  @Column(name = "ADDRESS_LINE2")
  public String addresLine2;

  @Column(name = "SUBURB", length = 60)
  public String suburb;

  @Column(name = "CITY", length = 60)
  public String city;

  @Column(name = "POSTAL_CODE", nullable = false, length = 10)
  public String postalCode;

  @Column(name = "LOCATION_PIN", length = 60)
  public String locationPin;

  @Column(name = "DATE_CREATED")
  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  public LocalDateTime dateCreated = LocalDateTime.now();
}
