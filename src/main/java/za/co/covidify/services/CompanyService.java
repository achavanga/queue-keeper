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
import za.co.covidify.model.Company;
import za.co.covidify.model.Person;

@ApplicationScoped
@Transactional(SUPPORTS)
public class CompanyService {

  @Inject
  AddressService addressService;

  @Inject
  PersonService personService;

  private static final Logger LOGGER = Logger.getLogger(CompanyService.class);

  public List<Company> findAllCompany() {
    return Company.listAll();
  }

  public Company findCompanyById(Long id) {
    return Company.findById(id);
  }

  @Transactional(REQUIRED)
  public Company createCompany(Company company) {
    if (company == null) {
      throw new WebApplicationException("Invalid request.", 422);
    }
    processPerson(company.contactPerson);
    processAddress(company.address);
    Person.persist(company);
    return company;
  }

  @Transactional(REQUIRED)
  public Company updateCompany(Company company) {
    if (company.id == null) {
      throw new WebApplicationException("Company was not set on request.", 422);
    }
    Company entity = Company.findById(company.id);

    if (entity == null) {
      throw new WebApplicationException("Company with id of " + company.id + " does not exist.", 404);
    }
    entity = company;
    return company;
  }

  private Person processPerson(Person person) {
    if (person == null) {
      throw new WebApplicationException("Invalid request.", 422);
    }
    else
      if (person.id != null || person.id != 0l) {
        person = personService.findPersonById(person.id);
      }
    return person;
  }

  private Address processAddress(Address address) {
    if (address == null) {
      throw new WebApplicationException("Invalid request.", 422);
    }
    else
      if (address.id == null || address.id == 0l) {
        address = addressService.createAddress(address);
      }
      else {
        address = addressService.findAddressById(address.id);
      }
    return address;
  }
}
