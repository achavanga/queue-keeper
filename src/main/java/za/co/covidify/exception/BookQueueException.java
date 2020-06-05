package za.co.covidify.exception;

import javax.ws.rs.NotAuthorizedException;

public class BookQueueException extends NotAuthorizedException {

  /**
   * 
   */
  private static final long serialVersionUID = -720710288906909990L;

  public BookQueueException(String message) {
    super(message);
  }
}