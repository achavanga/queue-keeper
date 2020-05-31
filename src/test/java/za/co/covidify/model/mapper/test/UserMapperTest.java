package za.co.covidify.model.mapper.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import za.co.covidify.model.User;
import za.co.covidify.model.dto.UserDto;
import za.co.covidify.model.mapper.UserMapper;

class UserMapperTest {

  @Test
  public void givenEmpDTONestedMappingToEmp_whenMaps_thenCorrect() {
    UserDto dto = new UserDto();
    dto.setUsername("test");
    User entity = UserMapper.INSTANCE.dtoToEntity(dto);
    assertEquals(dto.getUsername(), entity.username);
  }

}
