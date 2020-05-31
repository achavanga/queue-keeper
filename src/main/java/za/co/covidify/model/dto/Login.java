package za.co.covidify.model.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class Login {

  @NotEmpty(message = "User name cannot be empty")
  String username;

  @NotEmpty(message = "Password cannot be empty")
  String password;
}
