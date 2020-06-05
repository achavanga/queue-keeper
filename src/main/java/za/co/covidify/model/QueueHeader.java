package za.co.covidify.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Cacheable
@Table(name = "QUEUE_HEADER")
public class QueueHeader extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "queueHeaderSequence", sequenceName = "queue_id_seq", allocationSize = 1, initialValue = 10)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queueHeaderSequence")
  public Long id;

  @Column(name = "NAME")
  public String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "QUEUE_STATUS", nullable = false)
  public QueueStatus status = QueueStatus.ACTIVE;

  @Column(name = "REASON_FOR_STOPPING_QUEUE")
  public String reasonForStopping = "";

  @Column(name = "QUEUE_DATE")
  @JsonbTransient
  public LocalDateTime queueDate = LocalDateTime.now();

  @Column(name = "QUEUE_END_DATE")
  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  public LocalDateTime queueEndDateTime = LocalDateTime.now();

  @Column(name = "TOTAL_IN_QUEUE")
  public Long totalInQueue = 0l;

  @Column(name = "QUEUE_INTERVALS_IN_MINUTES")
  public int queueIntervalsInMinutes;

  @Column(name = "NUMBER_ALLOWED_AT_A_TIME")
  public int numberAllowedAtATime;

  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "COMPANY_ID")
  public Company company;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "queueHeader")
  @JsonbTransient
  public List<Queue> queue = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "CREATED_BY_USER_ID")
  public User createdBY;
}
