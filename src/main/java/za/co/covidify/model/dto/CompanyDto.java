package za.co.covidify.model.dto;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class CompanyDto {
  Long id;

  @NotNull
  String companyName;

  String websiteUrl;

  AddressDto addressDto;

  @NotNull
  String emailAddress;

  String cellphoneNumber;

  String phone;

  PersonDto contactPerson;

  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  LocalDateTime dateCreated = LocalDateTime.now();

  boolean isCompanyActive = false;

}
