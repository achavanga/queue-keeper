package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import io.quarkus.panache.common.Sort;
import za.co.covidify.model.Company;
import za.co.covidify.model.mapper.ModelMapper;
import za.co.covidify.response.to.CompanyRS;
import za.co.covidify.service.common.CommonServiceUtil;

@ApplicationScoped
@Transactional(SUPPORTS)
public class CompanyService {

  @Inject
  CommonServiceUtil commonServiceUtil;

  private static final Logger LOGGER = Logger.getLogger(CompanyService.class);

  public List<Company> findAllCompany() {
    return Company.listAll(Sort.by("companyName"));
  }

  public List<CompanyRS> findCompanyByName(String name) {
    return ModelMapper.INSTANCE.toCompanyRSs(Company.find("LOWER(companyName) like ?1", "%" + name.toLowerCase() + "%").list());
  }

  public CompanyRS findCompanyById(Long id) {
    return ModelMapper.INSTANCE.toCompanyRS(Company.findById(id));
  }

  public Optional<Company> findCompanyWithQueueHeaderByCompnayId(Long id) {
    return Company.find(
        "FROM Company c LEFT JOIN FETCH c.queueHeader q WHERE c.isCompanyActive = true and q.status = 'ACTIVE' and DATE(q.queueDate)= current_date and c.id = ?1",
        id).firstResultOptional();

  }

  @Transactional(REQUIRED)
  public CompanyRS createCompany(Company company) {
    if (company == null) {
      throw new WebApplicationException("Invalid request.", 400);
    }
    commonServiceUtil.processPerson(company.contactPerson, false);
    commonServiceUtil.processAddress(company.address, true);
    Company.persist(company);
    return ModelMapper.INSTANCE.toCompanyRS(company);
  }

  @Transactional
  public int updateCompany(Company company) {
    if (company.id == null) {
      throw new WebApplicationException("Company was not set on request.", 400);
    }

    Map<String, Object> params = new HashMap<>();
    params.put("companyName", company.companyName);
    params.put("logo", company.logo);
    params.put("id", company.id);
    params.put("emailAddress", company.emailAddress);
    params.put("websiteUrl", company.websiteUrl);
    params.put("cellphoneNumber", company.cellphoneNumber);

    return Company.update(
        "companyName = :companyName, logo =:logo, cellphoneNumber=:cellphoneNumber, " + "websiteUrl=:websiteUrl, emailAddress=:emailAddress  where id = :id",
        params);
  }

}
