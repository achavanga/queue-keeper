package za.co.covidify.response.to;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class PersonQueueRS {

  Long queueId;

  Long personId;

  @NotNull
  String queueNumber;

  String status;

  LocalDateTime expectedPorcessedTime;

  Long companyId;

  String companyName;

  String companylogo;

}
