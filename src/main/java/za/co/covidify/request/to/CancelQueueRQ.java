package za.co.covidify.request.to;

import lombok.Data;

@Data
public class CancelQueueRQ {

  long queueId;

  long persornId;

}
