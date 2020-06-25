package za.co.covidify.response.to;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
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

  AddressRS addressRS;

  @NotNull
  String emailAddress;

  String cellphoneNumber;

  String phone;

  PersonRS contactPerson;

  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  LocalDateTime dateCreated = LocalDateTime.now();

  boolean isCompanyActive = false;

}
