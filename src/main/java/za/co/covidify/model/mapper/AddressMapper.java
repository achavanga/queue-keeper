package za.co.covidify.model.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import za.co.covidify.model.Address;
import za.co.covidify.model.dto.AddressDto;

@Mapper
public interface AddressMapper {

  AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

  AddressDto toDto(Address address);

  @IterableMapping(elementTargetType = AddressDto.class)
  List<AddressDto> toDtos(List<Address> addresss);

  Address dtoToEntity(AddressDto addresstDto);
}
