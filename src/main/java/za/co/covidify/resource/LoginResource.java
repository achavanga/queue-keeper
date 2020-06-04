package za.co.covidify.resource;

import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import lombok.extern.slf4j.Slf4j;
import za.co.covidify.model.Login;
import za.co.covidify.model.User;
import za.co.covidify.services.LoginService;

@Slf4j
@Path("/api/v1/login")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Login Api", description = "User login operations.")
public class LoginResource {

  @Inject
  LoginService loginService;

  @Inject
  ReactiveMailer reactiveMailer;

  @POST
  @Counted(name = "countPostLogin", description = "How many calls have been performed")
  @Timed(name = "timePostLogin", description = "How long it takes to perform check.", unit = MetricUnits.MILLISECONDS)
  @Operation(summary = "Loging service ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = User.class)))
  @APIResponse(responseCode = "204", description = "No User with that username found")
  public Response login(@Valid Login login) throws InvalidKeySpecException {
    return loginService.login(login);
  }

  // @GET
  // @Path("/TermsAndConditionsMobile")
  // @Operation(summary = "TermsAndConditionsMobile")
  // @APIResponse(responseCode = "200")
  // @APIResponse(responseCode = "204", description = "No User with that
  // username found")
  // @Produces(MediaType.APPLICATION_OCTET_STREAM)
  // public Response export() {
  //
  // AddressCSV addressCSV = new AddressCSV("street", "town1", "country1");
  // return
  // Response.ok(addressCSV.toString()).header("Access-Control-Expose-Headers",
  // "content-disposition, Content-Type")
  // .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" +
  // "test.csv").build();
  //
  // }

  @GET
  @Path("/mail")
  // public Response sendASimpleEmail() {
  // mailer.send(Mail.withText("achavanga@gmail.com", "A simple email from
  // quarkus", "This is my body"));
  // return Response.accepted().build();
  // }
  public CompletionStage<Response> sendASimpleEmailAsync() {
    return reactiveMailer.send(Mail.withText("sudhir.gaikwad@outlook.com", "A reactive email from quarkus", "12 Testing sending emails."))
        .subscribeAsCompletionStage().thenApply(x -> Response.accepted().build());
  }

  @Provider
  public static class ErrorMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
      int code = 500;
      if (exception instanceof WebApplicationException) {
        code = ((WebApplicationException) exception).getResponse().getStatus();
      }
      return Response.status(code).entity(Json.createObjectBuilder().add("Error ", exception.getMessage()).add("Code ", code).build()).build();
    }

  }
}

class AddressCSV {

  private String street;

  private String town;

  private String country;

  public AddressCSV(String street, String town, String country) {
    this.street = street;
    this.town = town;
    this.country = country;
  }

  // getters and setters here

  // here you could have a method called generateCSV() for example

  // or you could override the toString() method like this

  @Override
  public String toString() {
    return street + "," + town + "," + country + "\n"; // Add the '\n' if you
                                                       // need a new line
  }
}