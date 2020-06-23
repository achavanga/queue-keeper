package za.co.covidify.request.to;

import lombok.Data;

@Data
public class PersonRS {

  long id;

  String name;

  String surname;

  String emailAddress;

  String cellphoneNumber;

}
