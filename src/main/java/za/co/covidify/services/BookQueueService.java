package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import za.co.covidify.model.BookQueue;
import za.co.covidify.model.BookQueueRs;
import za.co.covidify.model.Company;
import za.co.covidify.model.Person;
import za.co.covidify.model.Queue;
import za.co.covidify.model.QueueStatus;

@ApplicationScoped
@Transactional(SUPPORTS)
public class BookQueueService {

  int queueNumberStart = 100;

  @Inject
  QueueHeaderService queueHeaderService;

  @Inject
  CompanyService companyService;

  @Inject
  PersonService personService;

  @Transactional(REQUIRED)
  public BookQueueRs bookMyQueue(BookQueue bookQueue) {
    BookQueueRs bookqueueRs = new BookQueueRs();
    Person person = personService.findPersonById(bookQueue.getPersornId());
    if (person != null) {
      Company company = companyService.findCompanyWithQueueHeaderByCompnayId(bookQueue.getCompanyId());
      if (company.queueHeader.size() == 1) {
        generateQueueDetails(bookqueueRs, person, company);
      }
      else {
        /// throw error
      }
    }
    return bookqueueRs;
  }

  private void generateQueueDetails(BookQueueRs bookqueueRs, Person person, Company company) {
    Queue queue = new Queue();
    createQueue(person, company, queue);
    Queue.persist(queue);
    bookQueueResponse(bookqueueRs, company, queue);
  }

  private void createQueue(Person person, Company company, Queue queue) {
    queue.status = QueueStatus.CREATED;
    queue.queueHeader = company.queueHeader.get(0);
    queue.person = person;
    queue.queueNumber = "" + queueNumberStart;
    queue.expectedPorcessedTime = LocalDateTime.now().plusMinutes(company.queueHeader.get(0).queueIntervalsInMinutes);
  }

  private void bookQueueResponse(BookQueueRs bookqueueRs, Company company, Queue queue) {
    bookqueueRs.setQueueId(queue.id);
    bookqueueRs.setExpectedPorcessedTime(queue.expectedPorcessedTime);
    bookqueueRs.setQueuNumber(queue.queueNumber);
    bookqueueRs.setCompanyId(company.id);
  }

}
