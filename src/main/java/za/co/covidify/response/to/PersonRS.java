package za.co.covidify.response.to;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class PersonRS {

  long id;

  String name;

  String surname;

  String emailAddress;

  String cellphoneNumber;

  AddressRS address;

}
