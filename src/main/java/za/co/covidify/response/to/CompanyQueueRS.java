package za.co.covidify.response.to;

import java.time.LocalDateTime;
import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class CompanyQueueRS {

  long id;

  String queueName;

  int numberAllowedAtATime;

  Long totalInQueue;

  List<CompnayQueueListRS> queue;

  LocalDateTime queueDate;

  long companyId;

  String companyName;

  String logo = "";

}
