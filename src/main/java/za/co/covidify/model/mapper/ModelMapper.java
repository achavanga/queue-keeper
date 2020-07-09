package za.co.covidify.model.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import za.co.covidify.model.Address;
import za.co.covidify.model.Company;
import za.co.covidify.model.Person;
import za.co.covidify.model.Queue;
import za.co.covidify.model.QueueHeader;
import za.co.covidify.model.User;
import za.co.covidify.request.to.UserRQ;
import za.co.covidify.response.to.AddressRS;
import za.co.covidify.response.to.CompanyQueueRS;
import za.co.covidify.response.to.CompanyRS;
import za.co.covidify.response.to.CompnayQueueListRS;
import za.co.covidify.response.to.PersonQueueRS;
import za.co.covidify.response.to.PersonRS;
import za.co.covidify.response.to.QueueHeaderRS;
import za.co.covidify.response.to.QueueRS;
import za.co.covidify.response.to.UserRS;

@Mapper
public interface ModelMapper {

  ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

  AddressRS toDto(Address address);

  @IterableMapping(elementTargetType = AddressRS.class)
  List<AddressRS> toAddressDtos(List<Address> addresss);

  Address dtoToEntity(AddressRS addresstDto);

  UserRS toUserDto(User user);

  User dtoToUser(UserRS userRs);

  User toUser(UserRQ userRQ);

  QueueHeaderRS toQueueHeaderRS(QueueHeader queueHeader);

  QueueHeader dtoQueueHeader(QueueHeaderRS queueHeaderRS);

  @IterableMapping(elementTargetType = QueueHeaderRS.class)
  default List<QueueHeaderRS> toQueueHeaderRSs(List<QueueHeader> queueHeaders) {
    List<QueueHeaderRS> queueHeaderRSs = new ArrayList<>();
    for (QueueHeader queueHeader : queueHeaders) {
      QueueHeaderRS queueHeaderRS = toQueueHeaderRS(queueHeader);
      queueHeaderRSs.add(queueHeaderRS);
    }
    return queueHeaderRSs;
  }

  QueueRS toQueueRS(Queue queue);

  Queue dtoQueueHeader(QueueRS queueRS);

  @IterableMapping(elementTargetType = QueueRS.class)
  default List<QueueRS> toQueueRSs(List<Queue> queues) {
    List<QueueRS> queueRSs = new ArrayList<>();
    for (Queue queue : queues) {
      QueueRS queueRS = toQueueRS(queue);
      queueRSs.add(queueRS);
    }
    return queueRSs;
  }

  @Mapping(target = "personId", source = "person.id")
  @Mapping(target = "queueId", source = "id")
  @Mapping(target = "companyId", source = "queueHeader.company.id")
  @Mapping(target = "companyName", source = "queueHeader.company.companyName")
  @Mapping(target = "companylogo", source = "queueHeader.company.logo")
  PersonQueueRS toPersonQueueRS(Queue queue);

  default List<PersonQueueRS> toPersonQueueRSs(List<Queue> queues) {
    List<PersonQueueRS> personQueueRSs = new ArrayList<>();
    for (Queue queue : queues) {
      PersonQueueRS personQueueRS = toPersonQueueRS(queue);
      personQueueRSs.add(personQueueRS);
    }
    return personQueueRSs;
  }

  PersonRS toPersonRS(Person person);

  Person toPerson(PersonRS person);

  default List<PersonRS> toPersonRSs(List<Person> persons) {
    List<PersonRS> personRSs = new ArrayList<>();
    for (Person person : persons) {
      PersonRS personRS = toPersonRS(person);
      personRSs.add(personRS);
    }
    return personRSs;
  }

  @Mapping(target = "contactPersonId", source = "company.contactPerson.id")
  CompanyRS toCompanyRS(Company company);

  Company toCompany(CompanyRS company);

  default List<CompanyRS> toCompanyRSs(List<Company> companies) {
    List<CompanyRS> companyRSs = new ArrayList<>();
    for (Company company : companies) {
      CompanyRS companyRS = toCompanyRS(company);
      companyRSs.add(companyRS);
    }
    return companyRSs;
  }

  @Mapping(target = "name", source = "person.name")
  @Mapping(target = "surname", source = "person.surname")
  @Mapping(target = "personId", source = "person.id")
  CompnayQueueListRS toCompnayQueueListRS(Queue queue);

  @Mapping(target = "companyId", source = "company.id")
  @Mapping(target = "companyName", source = "company.companyName")
  @Mapping(target = "logo", source = "company.logo")
  CompanyQueueRS toCompanyQueueRS(QueueHeader queueHeader);

  default List<CompanyQueueRS> toCompanyQueueRSs(List<QueueHeader> queueHeaders) {
    List<CompanyQueueRS> companyQueueRSs = new ArrayList<>();
    for (QueueHeader queueHeader : queueHeaders) {
      CompanyQueueRS companyQueueRS = toCompanyQueueRS(queueHeader);
      List<CompnayQueueListRS> compnayQueueListRSs = new ArrayList<>();
      for (Queue queue : queueHeader.queue) {
        CompnayQueueListRS compnayQueueListRS = toCompnayQueueListRS(queue);
        compnayQueueListRSs.add(compnayQueueListRS);
      }
      companyQueueRS.setQueue(compnayQueueListRSs);
      companyQueueRSs.add(companyQueueRS);
    }
    return companyQueueRSs;
  }

}
