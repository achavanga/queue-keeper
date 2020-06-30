package za.co.covidify.request.to;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class BookQueueForOtherRQ {

  long companyId;

  String name;

  String surname;

  @Email
  String emailAddress;

  String cellphoneNumber;

}
