package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import za.co.covidify.exception.BookQueueException;
import za.co.covidify.exception.PersonException;
import za.co.covidify.exception.QueueException;
import za.co.covidify.model.Company;
import za.co.covidify.model.Person;
import za.co.covidify.model.Queue;
import za.co.covidify.model.QueueHeader;
import za.co.covidify.model.QueueStatus;
import za.co.covidify.request.to.BookQueueRQ;
import za.co.covidify.request.to.CancelQueueRQ;
import za.co.covidify.response.to.BookQueueRs;
import za.co.covidify.service.common.CommonServiceUtil;

@ApplicationScoped
@Transactional(SUPPORTS)
public class QueueService {

  private static final Logger LOGGER = Logger.getLogger(QueueService.class);

  Long queueNumberStart = 100l;

  @Inject
  CompanyService companyService;

  @Inject
  PersonService personService;

  @Inject
  CommonServiceUtil commonServiceUtil;

  public List<Queue> findAllQueue() {
    return Queue.listAll();
  }

  public Queue findQueueById(Long id) {
    return Queue.findById(id);
  }

  public Queue findQueueByNumber(Long id) {
    return Queue.findById(id);
  }

  @Transactional(REQUIRED)
  public Queue createQueue(Queue queue) {
    if (queue == null) {
      throw new WebApplicationException("Invalid request.", 422);
    }
    queue.person = commonServiceUtil.processPerson(queue.person, false);
    queue.queueHeader = commonServiceUtil.processQueueHeader(queue.queueHeader);
    return persistQueue(queue);
  }

  @Transactional(REQUIRED)
  public Queue updateQueue(Queue queue) {
    if (queue.id == null) {
      throw new WebApplicationException("Queue was not set on request.", 422);
    }
    Queue entity = Queue.findById(queue.id);

    if (entity == null) {
      throw new WebApplicationException("Queue with id of " + queue.id + " does not exist.", 404);
    }

    entity = queue;
    return queue;
  }

  @Transactional(REQUIRED)
  public BookQueueRs bookMyQueue(BookQueueRQ bookQueueRQ) {
    BookQueueRs bookqueueRs = new BookQueueRs();
    Person person = personService.findPersonById(bookQueueRQ.getPersornId());
    if (person != null) {
      Company company = companyService.findCompanyWithQueueHeaderByCompnayId(bookQueueRQ.getCompanyId());
      if (company.queueHeader.size() == 1) {
        generateQueueDetails(bookqueueRs, person, company);
        QueueHeader.update("totalInQueue = totalInQueue + 1  where id = ?1", company.queueHeader.get(0).id);
      }
      else {
        throw new BookQueueException("No Active queue for  " + company.companyName);
      }
    }
    else {
      throw new PersonException("Person cannot be emprty");
    }
    return bookqueueRs;
  }

  @Transactional(REQUIRED)
  public void cancelMyQueue(CancelQueueRQ cancelQueueRQ) {
    Queue entity = findQueueById(cancelQueueRQ.getQueueId());
    if (entity != null) {
      if (entity.person.id == cancelQueueRQ.getPersornId()) {
        entity.queueEndDateTime = LocalDateTime.now();
        entity.status = QueueStatus.CANCEL;
      }
      else {
        throw new PersonException("Invalid customer details");
      }
    }
    else {
      throw new QueueException("Cannot find the queue. Please use a valid queue number.");
    }
  }

  public Queue persistQueue(Queue queue) {
    Queue.persist(queue);
    return queue;
  }

  private void generateQueueDetails(BookQueueRs bookqueueRs, Person person, Company company) {
    Queue queue = new Queue();
    createQueue(person, company, queue);
    persistQueue(queue);
    bookQueueResponse(bookqueueRs, company, queue);
  }

  private void createQueue(Person person, Company company, Queue queue) {
    queue.status = QueueStatus.CREATED;
    queue.queueHeader = company.queueHeader.get(0);
    queue.person = person;
    long queueNumber = queue.queueHeader.totalInQueue + queueNumberStart;
    queue.queueNumber = "" + queueNumber;
    queue.expectedPorcessedTime = LocalDateTime.now().plusMinutes(company.queueHeader.get(0).queueIntervalsInMinutes);
  }

  private void bookQueueResponse(BookQueueRs bookqueueRs, Company company, Queue queue) {
    bookqueueRs.setQueueId(queue.id);
    bookqueueRs.setExpectedPorcessedTime(queue.expectedPorcessedTime);
    bookqueueRs.setQueueNumber(queue.queueNumber);
    bookqueueRs.setCompanyId(company.id);
  }

  @Transactional(REQUIRED)
  public void confirmMyQueue(@Valid CancelQueueRQ cancelQueueRQ) {
    Queue.update("status = 'ACTIVE'  where id = ?1 and person.id = ?2", cancelQueueRQ.getQueueId(), cancelQueueRQ.getPersornId());
  }
}
