package za.co.covidify.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import za.co.covidify.model.User;
import za.co.covidify.model.dto.UserDto;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserDto toDto(User user);

  // @IterableMapping(elementTargetType = UserDto.class)
  // @Mapping(target=, source = "manufacturingDate", dateFormat = "dd.MM.yyyy")
  List<UserDto> toDtos(List<User> users);

  User dtoToEntity(UserDto usertDto);

}
