package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import za.co.covidify.model.Person;
import za.co.covidify.model.User;
import za.co.covidify.util.Utils;

@ApplicationScoped
@Transactional(SUPPORTS)
public class UserService {

  @Inject
  PersonService personService;

  private static final Logger LOGGER = Logger.getLogger(UserService.class);

  public List<User> findAllUsers() {
    return User.listAll();
  }

  public User findUserById(Long id) {
    return User.findById(id);
  }

  public User findByName(String username) {
    return User.find("username", username).firstResult();
  }

  @Transactional(REQUIRED)
  public User createUser(User user) {
    if (user == null) {
      throw new WebApplicationException("Invalid request set on request.", 422);
    }
    user.password = Utils.generatePasswordHash(user.getPassword());
    processPerson(user);
    User.persist(user);
    return user;
  }

  @Transactional(REQUIRED)
  public User updateUser(User user) {
    if (user == null || user.id == null) {
      throw new WebApplicationException("User was not set on request.", 422);
    }
    User entity = User.findById(user.id);

    if (entity == null) {
      throw new WebApplicationException("User with id of " + user.id + " does not exist.", 404);
    }
    entity = user;
    return user;
  }

  private void processPerson(User user) {
    Person person = user.person;
    if (person == null) {
      throw new WebApplicationException("Invalid request.", 422);
    }
    else
      if (person.id == null || person.id == 0l) {
        person = personService.createPerson(person);
      }
      else {
        person = personService.findPersonById(person.id);
      }
    user.person = person;
  }

}
