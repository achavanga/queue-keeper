package za.co.covidify.response.to;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class QueueRS {
  Long id;

  @NotNull
  String queueNumber;

  String status;

  LocalDateTime queueDateTime = LocalDateTime.now();

  LocalDateTime expectedPorcessedTime;

  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  LocalDateTime queueEndDateTime;

  PersonRS person;

  // QueueHeaderDto queueHeader;

}
