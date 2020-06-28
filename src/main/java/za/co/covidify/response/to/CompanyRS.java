package za.co.covidify.response.to;

import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class CompanyRS {

  Long id;

  @NotNull
  String companyName;

  String websiteUrl;

  AddressRS address;

  @NotNull
  String emailAddress;

  String cellphoneNumber;

  String phone;

  long contactPersonId;

  // PersonRS contactPerson;

  String logo = "";

}
