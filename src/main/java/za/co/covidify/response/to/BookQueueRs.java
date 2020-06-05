package za.co.covidify.response.to;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;

import lombok.Data;

@Data
public class BookQueueRs {

  Long queueId;

  Long companyId;

  String queuNumber;

  @JsonbDateFormat("yyyy/MM/dd HH:mm:ss")
  LocalDateTime expectedPorcessedTime;

}
