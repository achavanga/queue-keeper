package za.co.covidify.model.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import za.co.covidify.model.Address;
import za.co.covidify.model.Queue;
import za.co.covidify.model.QueueHeader;
import za.co.covidify.model.User;
import za.co.covidify.request.to.QueueHeaderRS;
import za.co.covidify.request.to.QueueRS;
import za.co.covidify.response.to.AddressRS;
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
}
