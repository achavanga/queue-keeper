package za.co.covidify.request.to;

import lombok.Data;

@Data
public class VerifyQueueRQ {

  long companyId;

  long queueId;

}
