package za.co.covidify.model.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import za.co.covidify.model.Person;
import za.co.covidify.model.dto.PersonDto;

@Mapper
public interface PersonMapper {

  PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

  PersonDto toDto(Person person);

  @IterableMapping(elementTargetType = PersonDto.class)
  List<PersonDto> toDtos(List<Person> persons);

  Person dtoToEntity(PersonDto persontDto);
}
