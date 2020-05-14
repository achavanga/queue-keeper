package za.co.covidify.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Cacheable
@Table(name = "QueueHeader")
public class QueueHeader extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "queueHeaderSequence", sequenceName = "queue_id_seq", allocationSize = 1, initialValue = 2)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queueHeaderSequence")
  public Long id;

  @Column(name = "QUEUE_NUMBER", length = 20)
  public String queueNumber;

  public String getQueueNumber() {
    return String.format("%020d", this.id);
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "QUEUE_STATUS", nullable = false)
  public QueueStatus status = QueueStatus.ACTIVE;

  @Column(name = "QUEUE_START_DATE")
  @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
  public LocalDate queueSatrtDateTime;

  @Column(name = "QUEUE_END_DATE")
  @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
  public LocalDate queueEndDateTime;

  @Column(name = "TOTAL_IN_QUEUE")
  public Long totalInQueue;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMPANY_ID")
  private Company company;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "QUEUE_HEADER_QUEUE", joinColumns = @JoinColumn(name = "QUEUE_ID"), inverseJoinColumns = @JoinColumn(name = "QUEUE_HEADER_ID"))
  public Set<Queue> queue = new HashSet<>();

}
