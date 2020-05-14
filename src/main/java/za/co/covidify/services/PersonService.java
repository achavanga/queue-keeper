package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import io.quarkus.panache.common.Sort;
import za.co.covidify.model.Person;

@ApplicationScoped
@Transactional(SUPPORTS)
public class PersonService {

  private static final Logger LOGGER = Logger.getLogger(PersonService.class);

  public List<Person> findAllPersons() {
    return Person.listAll(Sort.by("surname"));
  }

  public Person findPersonById(Long id) {
    return Person.findById(id);
  }

  @Transactional(REQUIRED)
  public Person createPerson(Person person) {
    if (person == null) {
      throw new WebApplicationException("Id was invalidly set on request.", 422);
    }
    Person.persist(person);
    return person;
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
