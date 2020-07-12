package za.co.covidify.request.to;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class CancelQueueHeaderRQ {

  @Positive(message = "Queu Header should and a positive number")
  long queueHeaderId;

  @NotEmpty(message = "Reason can not be empty")
  String cancelReasons;

}
