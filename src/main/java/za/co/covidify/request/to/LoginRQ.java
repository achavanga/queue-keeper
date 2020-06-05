package za.co.covidify.request.to;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class LoginRQ {

  @NotEmpty(message = "User name cannot be empty")
  String username;

  @NotEmpty(message = "Password cannot be empty")
  String password;
}
