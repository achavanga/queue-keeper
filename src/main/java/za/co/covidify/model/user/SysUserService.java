package za.co.covidify.model.user;

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

@ApplicationScoped
@Transactional(SUPPORTS)
public class SysUserService {
  private static final Logger LOGGER = Logger.getLogger(SysUserService.class);

  public List<SysUser> findAllUsers() {
    return SysUser.listAll(Sort.by("person.surname"));
  }

  public SysUser findUserById(Long id) {
    return SysUser.findById(id);

  }

  @Transactional(REQUIRED)
  public SysUser createUser(SysUser user) {
    Person person = new Person();
    if (user == null || user.id != null) {
      throw new WebApplicationException("Id was invalidly set on request.", 422);
    }
    if (user.person.id == null || user.person.id == 0) {
      person = user.person;
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
      Person.persist(person);
    }
    else {
      person = Person.findById(user.person.id);
    }

    user.person = person;
    SysUser.persist(user);

    return user;
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
