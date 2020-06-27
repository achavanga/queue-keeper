package za.co.covidify.request.to;

import javax.json.bind.annotation.JsonbTransient;
import javax.validation.constraints.Email;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class UserRQ {

  Long id;

  String password;

  String username;

  String type = "GENERAL_USER";

  String name;

  String surname;

  @Email
  String emailAddress;

  String cellphoneNumber;

  String locationPin;

  String postalCode;

  @JsonbTransient
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}