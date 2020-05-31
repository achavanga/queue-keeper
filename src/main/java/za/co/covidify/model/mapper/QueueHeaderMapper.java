package za.co.covidify.model.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import za.co.covidify.model.QueueHeader;
import za.co.covidify.model.dto.QueueHeaderDto;

@Mapper
public interface QueueHeaderMapper {

  QueueHeaderMapper INSTANCE = Mappers.getMapper(QueueHeaderMapper.class);

  QueueHeaderDto toDto(QueueHeader queueHeader);

  @IterableMapping(elementTargetType = QueueHeaderDto.class)
  List<QueueHeaderDto> toDtos(List<QueueHeader> queueHeaders);

  QueueHeader dtoToEntity(QueueHeaderDto queueHeadertDto);
}
