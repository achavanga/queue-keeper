package za.co.covidify.response.to;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import za.co.covidify.request.to.CompanyRS;

@Data
@RegisterForReflection
public class QueueHeaderRS {

  long id;

  @NotNull(message = "Queue name cannot be empty")
  String queueName;

  int queueIntervalsInMinutes;

  int numberAllowedAtATime;

  Long totalInQueue = 0l;

  LocalDateTime queueDate;

  CompanyRS company;

}
