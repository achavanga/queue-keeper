package za.co.covidify.model;

import java.time.LocalDate;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

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

  @Enumerated(EnumType.STRING)
  @Column(name = "QUEUE_STATUS", nullable = false)
  public QueueStatus status = QueueStatus.ACTIVE;

  @Column(name = "QUEUE_DATE")
  @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
  public LocalDate queueDateTime;

  @Column(name = "PROCESSED_DATE")
  @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
  public LocalDate processedDateTime;

  @ManyToOne
  @JoinColumn(name = "PERSON_ID")
  private Person person;

  @ManyToOne
  @JoinColumn(name = "QUEUE_HEADER_ID")
  private QueueHeader queueHeader;

}
