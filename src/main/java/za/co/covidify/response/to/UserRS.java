package za.co.covidify.response.to;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class UserRS {

  long id;

  String username;

  String type;

  PersonRS person;

  String status;

  Long companyId;

}
