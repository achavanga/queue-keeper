package za.co.covidify.model.mapper.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import za.co.covidify.model.User;
import za.co.covidify.model.mapper.ModelMapper;
import za.co.covidify.response.to.UserRS;

class UserMapperTest {

  @Test
  public void givenEmpDTONestedMappingToEmp_whenMaps_thenCorrect() {
    UserRS dto = new UserRS();
    dto.setUsername("test");
    User entity = ModelMapper.INSTANCE.dtoToUser(dto);
    assertEquals(dto.getUsername(), entity.username);
  }

}
