package za.co.covidify.model.business;

import java.time.LocalDate;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import za.co.covidify.model.person.Person;

@Entity
@Cacheable
@Table(name = "QUEUE")
public class Queue extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "queueSequence", sequenceName = "queue_id_seq", allocationSize = 1, initialValue = 2)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queueSequence")
  public Long id;

  @Column(name = "QUEUE_NUMBER", length = 10)
  public String queueNumber;

  public String getQueueNumber() {
    return String.format("%010d", this.id);
  }

  @OneToOne(targetEntity = Person.class, cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "PERSON_ID")
  public Person person;

  @Column(name = "QUEUE_DATE_TIME")
  public LocalDate queueDateTime;

}
