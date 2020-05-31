package za.co.covidify.model.dto;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class UserDto {

  Long id;

  @NotNull
  String username;

  @NotNull
  @Size(min = 3, max = 150)
  String password;

  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  LocalDateTime lastSignedIn = LocalDateTime.now();

  String type;

  PersonDto person;

  String status = "ACTIVE";

  @JsonbTransient
  String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
