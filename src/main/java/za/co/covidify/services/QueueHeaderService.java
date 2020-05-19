package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import za.co.covidify.model.Company;
import za.co.covidify.model.QueueHeader;

@ApplicationScoped
@Transactional(SUPPORTS)
public class QueueHeaderService {

  @Inject
  CompanyService companyService;

  private static final Logger LOGGER = Logger.getLogger(QueueHeaderService.class);

  public List<QueueHeader> findAllQueueHeader() {
    return QueueHeader.listAll();
  }

  public QueueHeader findQueueHeaderById(Long id) {
    return QueueHeader.findById(id);
  }

  @Transactional(REQUIRED)
  public QueueHeader createQueueHeader(QueueHeader queueHeader) {
    if (queueHeader == null) {
      throw new WebApplicationException("Invalid request.", 422);
    }
    processCompany(queueHeader.company);
    QueueHeader.persist(queueHeader);
    return queueHeader;
  }

  @Transactional(REQUIRED)
  public QueueHeader updateQueueHeader(QueueHeader queueHeader) {
    if (queueHeader.id == null) {
      throw new WebApplicationException("Queue was not set on request.", 422);
    }
    QueueHeader entity = QueueHeader.findById(queueHeader.id);

    if (entity == null) {
      throw new WebApplicationException("Queue with id of " + queueHeader.id + " does not exist.", 404);
    }

    entity = queueHeader;
    return queueHeader;
  }

  private Company processCompany(Company company) {
    if (company == null) {
      throw new WebApplicationException("Invalid request.", 422);
    }
    else
      if (company.id != null || company.id != 0l) {
        company = companyService.findCompanyById(company.id);
      }
    return company;
  }
}
