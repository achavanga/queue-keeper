package za.co.covidify.model.dto;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class PersonDto {
  Long id;

  String name;

  String surname;

  @Email
  @NotNull
  @Size(max = 200)
  String emailAddress;

  String cellphoneNumber;

  String idNumber;

  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  LocalDateTime dateCreated = LocalDateTime.now();

  AddressDto address;

  CompanyDto company;

}
