package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import za.co.covidify.model.Company;
import za.co.covidify.model.QueueHeader;
import za.co.covidify.model.QueueStatus;
import za.co.covidify.model.User;
import za.co.covidify.model.mapper.ModelMapper;
import za.co.covidify.request.to.CancelQueueHeaderRQ;
import za.co.covidify.request.to.CreateQueueHeaderRQ;
import za.co.covidify.response.to.QueueHeaderRS;
import za.co.covidify.service.common.CommonServiceUtil;

@ApplicationScoped
@Transactional(SUPPORTS)
public class QueueHeaderService {

  @Inject
  CommonServiceUtil commonServiceUtil;

  @Inject
  UserService userService;

  private static final Logger LOGGER = Logger.getLogger(QueueHeaderService.class);

  public List<QueueHeaderRS> findAllQueueHeader() {
    return ModelMapper.INSTANCE.toQueueHeaderRSs(QueueHeader.listAll());
  }

  public QueueHeaderRS findQueueHeaderById(Long id) {
    return ModelMapper.INSTANCE.toQueueHeaderRS(QueueHeader.findById(id));
  }

  public List<QueueHeaderRS> findQueueHeaderByCompnayId(Long id) {
    return ModelMapper.INSTANCE.toQueueHeaderRSs(QueueHeader.find("company.id = ?1 and ", id).list());
  }

  @Transactional(REQUIRED)
  public QueueHeader updateQueueHeader(QueueHeader queueHeader) {
    if (queueHeader.id == null) {
      throw new WebApplicationException("Queue was not set on request.", 400);
    }
    QueueHeader entity = QueueHeader.findById(queueHeader.id);

    if (entity == null) {
      throw new WebApplicationException("Queue with id of " + queueHeader.id + " does not exist.", 204);
    }

    entity = queueHeader;
    return queueHeader;
  }

  @Transactional(REQUIRED)
  public QueueHeaderRS createQueueHeader(CreateQueueHeaderRQ createQueueHeaderRQ) {
    Optional<Company> companyQueueHeader = commonServiceUtil.findCompanyWithQueueHeaderByCompnayId(createQueueHeaderRQ.getCompanyid());
    if (!companyQueueHeader.isPresent()) {
      Company company = commonServiceUtil.processCompany(createQueueHeaderRQ.getCompanyid());
      User user = userService.findUserById(createQueueHeaderRQ.getUserid());
      if (company != null && user != null) {
        QueueHeader queueHeader = new QueueHeader();
        queueHeader.company = company;
        queueHeader.name = createQueueHeaderRQ.getQueueName();
        queueHeader.numberAllowedAtATime = createQueueHeaderRQ.getNumberAllowedAtATime();
        queueHeader.queueDate = LocalDateTime.now();
        queueHeader.queueIntervalsInMinutes = createQueueHeaderRQ.getQueueIntervalsInMinutes();
        queueHeader.status = QueueStatus.ACTIVE;
        queueHeader.totalInQueue = 0l;
        queueHeader.createdBY = user;
        QueueHeader.persist(queueHeader);
        return ModelMapper.INSTANCE.toQueueHeaderRS(queueHeader);
      }
      else {
        throw new WebApplicationException("Invalid request. Company not found", 400);
      }
    }
    else {
      throw new WebApplicationException("An active queue setup alread exist for the day.", 400);
    }
  }

  @Transactional(REQUIRED)
  public void cancelQueueHeader(CancelQueueHeaderRQ cancelQueueHeaderRQ) {
    QueueHeader entity = QueueHeader.findById(cancelQueueHeaderRQ.getQueueHeaderId());
    if (entity != null) {
      entity.reasonForStopping = cancelQueueHeaderRQ.getCancelReasons();
      entity.queueEndDateTime = LocalDateTime.now();
      entity.status = QueueStatus.CANCELlED;
    }
    else {
      throw new WebApplicationException("Invalid Queue details", 400);
    }
  }
}
