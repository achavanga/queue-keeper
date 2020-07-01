package za.co.covidify.response.to;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CompnayQueueListRS {

  Long id;

  String queueNumber;

  String status;

  LocalDateTime expectedPorcessedTime;

  long personId;

  String name;

  String surname;

}
