package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import za.co.covidify.model.Address;
import za.co.covidify.model.Person;
import za.co.covidify.model.User;
import za.co.covidify.model.UserStatus;
import za.co.covidify.model.mapper.ModelMapper;
import za.co.covidify.request.to.UserRQ;
import za.co.covidify.service.common.CommonServiceUtil;
import za.co.covidify.util.Utils;

@ApplicationScoped
@Transactional(SUPPORTS)
public class UserService {

  @Inject
  CommonServiceUtil commonServiceUtil;

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
  public User createUser(UserRQ userRQ) {
    User user = new User();
    if (userRQ == null) {
      throw new WebApplicationException("Invalid request set on request.", 400);
    }

    user = ModelMapper.INSTANCE.toUser(userRQ);

    user.password = Utils.generatePasswordHash(userRQ.getPassword());
    Person person = new Person();
    person.name = userRQ.getName();
    person.surname = userRQ.getSurname();
    person.emailAddress = userRQ.getEmailAddress();
    person.cellphoneNumber = userRQ.getCellphoneNumber();

    Address address = new Address();
    address.locationPin = userRQ.getLocationPin();
    address.postalCode = userRQ.getPostalCode();
    address.addressLine = userRQ.getPostalCode();

    person.address = address;
    user.person = person;
    user.status = UserStatus.ACTIVE;

    commonServiceUtil.processPerson(user.person, true);
    User.persist(user);
    return user;// ModelMapper.INSTANCE.toUserDto(user);
  }

  @Transactional(REQUIRED)
  public User updateUser(User user) {
    if (user == null || user.id == null) {
      throw new WebApplicationException("User was not set on request.", 400);
    }
    User entity = User.findById(user.id);

    if (entity == null) {
      throw new WebApplicationException("User with id of " + user.id + " does not exist.", 204);
    }
    entity = user;
    return user;
  }

}
