package za.co.covidify.service.common;

import static javax.transaction.Transactional.TxType.SUPPORTS;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import za.co.covidify.model.Address;
import za.co.covidify.model.Company;
import za.co.covidify.model.Person;
import za.co.covidify.model.QueueHeader;
import za.co.covidify.services.AddressService;
import za.co.covidify.services.CompanyService;
import za.co.covidify.services.PersonService;

@ApplicationScoped
@Transactional(SUPPORTS)
public class CommonServiceUtil {

  @Inject
  PersonService personService;

  @Inject
  CompanyService companyService;

  @Inject
  AddressService addressService;

  // @Inject
  // QueueHeaderService queueHeaderService;

  /**
   * 
   * @param person
   * @param isCreate
   * @return
   */
  public Person processPerson(Person person, boolean isCreate) {
    if (person == null) {
      throw new WebApplicationException("Invalid Person details  request.", 422);
    }
    else
      if (isCreate && (person.id == null || person.id == 0l)) {
        person = personService.createPerson(person);
      }
      else
        if (person.id != null || person.id != 0l) {
          person = personService.findPersonById(person.id);
        }
        else {
          throw new WebApplicationException("Invalid Person details request.", 422);
        }
    return person;
  }

  /**
   * 
   * @param company
   * @return
   */
  public Company processCompany(long id) {
    if (id != 0l) {
      return companyService.findCompanyById(id);
    }
    return null;
  }

  /**
   * 
   * @param address
   * @return
   */
  public Address processAddress(Address address, boolean isCreate) {
    if (address == null) {
      throw new WebApplicationException("Invalid Address details request.", 422);
    }
    else
      if (isCreate && (address.id == null || address.id == 0l)) {
        address = addressService.createAddress(address);
      }
      else
        if (address.id != null && address.id != 0l) {
          address = addressService.findAddressById(address.id);
        }
        else {
          throw new WebApplicationException("Invalid Address details request.", 422);
        }
    return address;
  }

  public QueueHeader processQueueHeader(QueueHeader queueHeader) {
    if (queueHeader == null) {
      throw new WebApplicationException("Invalid Queue Header request.", 422);
    }
    else
      if (queueHeader.id != null || queueHeader.id != 0l) {
        queueHeader = QueueHeader.findById(queueHeader.id);
      }
    return queueHeader;
  }
}
