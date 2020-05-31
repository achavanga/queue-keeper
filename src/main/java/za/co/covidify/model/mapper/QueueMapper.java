package za.co.covidify.model.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import za.co.covidify.model.Queue;
import za.co.covidify.model.dto.QueueDto;

@Mapper
public interface QueueMapper {

  QueueMapper INSTANCE = Mappers.getMapper(QueueMapper.class);

  QueueDto toDto(Queue queue);

  @IterableMapping(elementTargetType = QueueDto.class)
  List<QueueDto> toDtos(List<Queue> queues);

  Queue dtoToEntity(QueueDto queuetDto);
}
