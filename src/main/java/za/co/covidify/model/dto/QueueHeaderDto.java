package za.co.covidify.model.dto;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class QueueHeaderDto {

  Long id;

  @NotNull
  String name;

  String queuePrefix;

  String status = "ACTIVE";

  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  LocalDateTime queueDate = LocalDateTime.now();

  int queueIntervalsInMinutes;

  int numberAllowedAtATime;

  String reasonForStopping;

  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  LocalDateTime queueEndDateTime = LocalDateTime.now();

  Long totalInQueue;

  CompanyDto companyDto;

  PersonDto createdBY;
}
