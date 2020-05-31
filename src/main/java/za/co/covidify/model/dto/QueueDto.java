package za.co.covidify.model.dto;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class QueueDto {
  Long id;

  @NotNull
  String queueNumber;

  String status;

  LocalDateTime queueDateTime = LocalDateTime.now();

  LocalDateTime expectedPorcessedTime;

  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  LocalDateTime queueEndDateTime;

  PersonDto person;

  QueueHeaderDto queueHeader;

}
