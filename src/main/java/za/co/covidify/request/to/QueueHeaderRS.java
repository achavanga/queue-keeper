package za.co.covidify.request.to;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class QueueHeaderRS {

  @NotNull(message = "Queue name cannot be empty")
  String queueName;

  int queueIntervalsInMinutes;

  int numberAllowedAtATime;

  long companyid;

  Long totalInQueue = 0l;

  LocalDateTime queueDate;

  CompanyRS company;
}
