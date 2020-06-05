package za.co.covidify.response.to;

import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class AddressRS {

  long id;

  @NotNull
  String addressLine;

  String addresLine2;

  String suburb;

  String city;

  String postalCode;

  String locationPin;
}
