package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import za.co.covidify.model.Address;

@ApplicationScoped
@Transactional(SUPPORTS)
public class AddressService {
  private static final Logger LOGGER = Logger.getLogger(AddressService.class);

  public List<Address> findAllAddress() {
    return Address.listAll();
  }

  public Address findAddressById(Long id) {
    return Address.findById(id);
  }

  @Transactional(REQUIRED)
  public Address createAddress(Address address) {
    if (address == null) {
      throw new WebApplicationException("Invalid request.", 400);
    }
    Address.persist(address);
    return address;
  }

  @Transactional(REQUIRED)
  public Address updateAddress(Address address) {
    if (address.id == null) {
      throw new WebApplicationException("Address was not set on request.", 400);
    }
    Address entity = Address.findById(address.id);

    if (entity == null) {
      throw new WebApplicationException("Address with id of " + address.id + " does not exist.", 204);
    }

    entity = address;
    return address;
  }
}
