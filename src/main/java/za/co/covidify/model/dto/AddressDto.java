package za.co.covidify.model.dto;

import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class AddressDto {
  Long id;

  @NotNull
  String addressLine;

  String addresLine2;

  String suburb;

  String city;

  String postalCode;

  String locationPin;
}
