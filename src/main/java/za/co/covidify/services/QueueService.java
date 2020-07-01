package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import za.co.covidify.model.Address;
import za.co.covidify.model.Company;
import za.co.covidify.model.Person;
import za.co.covidify.model.Queue;
import za.co.covidify.model.QueueHeader;
import za.co.covidify.model.QueueStatus;
import za.co.covidify.model.mapper.ModelMapper;
import za.co.covidify.request.to.BookQueueForOtherRQ;
import za.co.covidify.request.to.BookQueueRQ;
import za.co.covidify.request.to.CancelQueueRQ;
import za.co.covidify.response.to.BookQueueRs;
import za.co.covidify.response.to.PersonQueueRS;
import za.co.covidify.response.to.QueueRS;
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

  public List<QueueRS> findAllQueue() {
    return ModelMapper.INSTANCE.toQueueRSs(Queue.listAll());
  }

  public QueueRS findQueueById(Long id) {
    return ModelMapper.INSTANCE.toQueueRS(Queue.findById(id));
  }

  @Transactional(REQUIRED)
  public Queue updateQueue(Queue queue) {
    if (queue.id == null) {
      throw new WebApplicationException("Queue was not set on request.", 400);
    }
    Queue entity = Queue.findById(queue.id);

    if (entity == null) {
      throw new WebApplicationException("Queue with id of " + queue.id + " does not exist.", 204);
    }
    entity = queue;
    return queue;
  }

  public List<PersonQueueRS> findQueueByPersonId(Long id) {
    return ModelMapper.INSTANCE.toPersonQueueRSs(Queue.find("person.id = ?1", id).list());
  }

  @Transactional(REQUIRED)
  public BookQueueRs bookQueueForOther(BookQueueForOtherRQ bookQueueForOtherRQ) {
    Person person = new Person();
    person.name = bookQueueForOtherRQ.getName();
    person.surname = bookQueueForOtherRQ.getSurname();
    person.emailAddress = bookQueueForOtherRQ.getEmailAddress();
    person.cellphoneNumber = bookQueueForOtherRQ.getCellphoneNumber();

    Address address = new Address();
    address.addressLine = " ";
    address.locationPin = "000";
    address.postalCode = "00";
    person.address = address;
    commonServiceUtil.processPerson(person, true);

    BookQueueRQ bookQueueRQ = new BookQueueRQ();
    bookQueueRQ.setCompanyId(bookQueueForOtherRQ.getCompanyId());
    bookQueueRQ.setPersornId(person.id);
    return bookMyQueue(bookQueueRQ);
  }

  @Transactional(REQUIRED)
  public BookQueueRs bookMyQueue(BookQueueRQ bookQueueRQ) {
    BookQueueRs bookqueueRs = new BookQueueRs();
    Person person = Person.findById(bookQueueRQ.getPersornId());
    if (person != null) {
      Optional<Company> company = companyService.findCompanyWithQueueHeaderByCompnayId(bookQueueRQ.getCompanyId());
      if (company.isPresent()) {
        generateQueueDetails(bookqueueRs, person, company.get());
        QueueHeader.update("totalInQueue = totalInQueue + 1  where id = ?1", company.get().queueHeader.get(0).id);
      }
      else {
        throw new WebApplicationException("No Active queue for company with ID  " + bookQueueRQ.getCompanyId(), 204);
      }
    }
    else {
      throw new WebApplicationException("Person cannot be emprty", 400);
    }
    return bookqueueRs;
  }

  @Transactional(REQUIRED)
  public void cancelMyQueue(CancelQueueRQ cancelQueueRQ) {
    Queue entity = Queue.findById(cancelQueueRQ.getQueueId());
    if (entity != null) {
      if (entity.person.id == cancelQueueRQ.getPersornId()) {
        entity.queueEndDateTime = LocalDateTime.now();
        entity.status = QueueStatus.CANCELLED;
      }
      else {
        throw new WebApplicationException("Invalid customer details", 400);
      }
    }
    else {
      throw new WebApplicationException("Cannot find the queue. Please use a valid queue number.", 400);
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
