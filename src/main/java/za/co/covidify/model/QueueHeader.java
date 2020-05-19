package za.co.covidify.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonFormat;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Cacheable
@Table(name = "QUEUE_HEADER")
public class QueueHeader extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "queueHeaderSequence", sequenceName = "queue_id_seq", allocationSize = 1, initialValue = 2)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queueHeaderSequence")
  public Long id;

  @Column(name = "QUEUE_NUMBER", length = 20)
  public String queueNumber = String.format("%020d", this.id);

  @Enumerated(EnumType.STRING)
  @Column(name = "QUEUE_STATUS", nullable = false)
  public QueueStatus status = QueueStatus.ACTIVE;

  @Column(name = "QUEUE_START_DATE")
  @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
  public LocalDateTime queueSatrtDateTime = LocalDateTime.now();

  @Column(name = "QUEUE_END_DATE")
  @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
  public LocalDateTime queueEndDateTime = LocalDateTime.now();

  @Column(name = "TOTAL_IN_QUEUE")
  public Long totalInQueue;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMPANY_ID")
  public Company company;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "queueHeader")
  @JsonbTransient
  public List<Queue> queue = new ArrayList<>();

}
