package za.co.covidify.exception;

import javax.ws.rs.NotAuthorizedException;

@SuppressWarnings("serial")
public class PersonException extends NotAuthorizedException {

  public PersonException(String message) {
    super(message);
  }
}