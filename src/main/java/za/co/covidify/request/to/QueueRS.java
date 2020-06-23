package za.co.covidify.request.to;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class QueueRS {

  Long id;

  String queueNumber;

  String status;

  LocalDateTime expectedPorcessedTime;

  PersonRS person;

  QueueHeaderRS queueHeader;

}
