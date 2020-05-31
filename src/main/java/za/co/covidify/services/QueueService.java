package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import za.co.covidify.model.Queue;
import za.co.covidify.service.common.CommonServiceUtil;

@ApplicationScoped
@Transactional(SUPPORTS)
public class QueueService {

  private static final Logger LOGGER = Logger.getLogger(QueueService.class);

  @Inject
  CommonServiceUtil commonServiceUtil;

  public List<Queue> findAllQueue() {
    return Queue.listAll();
  }

  public Queue findQueueById(Long id) {
    return Queue.findById(id);
  }

  @Transactional(REQUIRED)
  public Queue createQueue(Queue queue) {
    if (queue == null) {
      throw new WebApplicationException("Invalid request.", 422);
    }
    queue.person = commonServiceUtil.processPerson(queue.person, false);
    queue.queueHeader = commonServiceUtil.processQueueHeader(queue.queueHeader);
    Queue.persist(queue);
    return queue;
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

}
