package za.co.covidify.model.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import za.co.covidify.model.Address;
import za.co.covidify.model.User;
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
}
