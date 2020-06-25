package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import io.quarkus.panache.common.Sort;
import za.co.covidify.model.Person;
import za.co.covidify.model.mapper.ModelMapper;
import za.co.covidify.response.to.PersonRS;
import za.co.covidify.service.common.CommonServiceUtil;

@ApplicationScoped
@Transactional(SUPPORTS)
public class PersonService {

  @Inject
  CommonServiceUtil commonServiceUtil;

  private static final Logger LOGGER = Logger.getLogger(PersonService.class);

  public List<PersonRS> findAllPersons() {
    return ModelMapper.INSTANCE.toPersonRSs(Person.listAll(Sort.by("surname")));
  }

  public PersonRS findPersonById(Long id) {
    return ModelMapper.INSTANCE.toPersonRS(Person.findById(id));
  }

  @Transactional(REQUIRED)
  public PersonRS createPerson(Person person) {
    if (person == null) {
      throw new WebApplicationException("Id was invalidly set on request.", 422);
    }
    commonServiceUtil.processAddress(person.address, true);
    Person.persist(person);
    return ModelMapper.INSTANCE.toPersonRS(person);
  }

  @Transactional(REQUIRED)
  public Person updatePerson(Person person) {
    if (person.id == null) {
      throw new WebApplicationException("User was not set on request.", 422);
    }
    Person entity = Person.findById(person.id);

    if (entity == null) {
      throw new WebApplicationException("User with id of " + person.id + " does not exist.", 404);
    }
    entity = person;
    return person;
  }

}
