package za.co.covidify.exception;

import javax.ws.rs.NotAuthorizedException;

@SuppressWarnings("serial")
public class QueueException extends NotAuthorizedException {

  public QueueException(String message) {
    super(message);
  }
}