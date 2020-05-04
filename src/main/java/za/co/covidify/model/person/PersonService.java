package za.co.covidify.model.person;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import io.quarkus.panache.common.Sort;
import za.co.covidify.model.contact.Address;
import za.co.covidify.model.user.SysUser;

@ApplicationScoped
@Transactional(SUPPORTS)
public class PersonService {

  private static final Logger LOGGER = Logger.getLogger(Person.class);

  public List<Person> findAllPersons() {
    return Person.listAll(Sort.by("surname"));
  }

  public Person findPersonById(Long id) {
    return Person.findById(id);
  }

  @Transactional(REQUIRED)
  public Person createUser(Person person) {

    if (person.id == null || person.id == 0) {
      Set<Address> addresses = new HashSet<>();
      for (Address address : person.addresses) {
        if (address.id == null || address.id == 0) {
          Address.persist(address);
        }
        else {
          address = Address.findById(address.id);
        }
        addresses.add(address);
      }
      person.addresses = addresses;
    }
    else {
      person = Person.findById(person.id);
    }

    Person.persist(person);

    return person;
  }

  @Transactional(REQUIRED)
  public SysUser updateUser(SysUser user) {
    if (user.id == null) {
      throw new WebApplicationException("User was not set on request.", 422);
    }

    SysUser entity = SysUser.findById(user.id);

    if (entity == null) {
      throw new WebApplicationException("User with id of " + user.id + " does not exist.", 404);
    }

    entity = user;

    return user;
  }
}
