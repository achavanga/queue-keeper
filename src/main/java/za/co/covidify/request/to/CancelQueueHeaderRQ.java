package za.co.covidify.request.to;

import lombok.Data;

@Data
public class CancelQueueHeaderRQ {

  long queueHeaderId;

  String cancelReasons;

}
