package za.co.covidify.request.to;

import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class CreateQueueHeaderRQ {

  @NotNull(message = "Queue name cannot be empty")
  String queueName;

  int queueIntervalsInMinutes;

  int numberAllowedAtATime;

  long companyid;

  long userid;
}
