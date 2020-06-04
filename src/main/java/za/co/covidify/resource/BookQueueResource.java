package za.co.covidify.resource;

import java.security.spec.InvalidKeySpecException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import za.co.covidify.model.BookQueue;
import za.co.covidify.model.User;
import za.co.covidify.services.BookQueueService;

@Path("/api/v1/queue/book/")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Queue Booking Api", description = "Queue Booking details operations.")
public class BookQueueResource {

  @Inject
  BookQueueService bookQueueService;

  @POST
  @Counted(name = "countPost_BookQueue", description = "How many calls have been performed")
  @Timed(name = "timePost_BookQueue", description = "How long it takes to perform check.", unit = MetricUnits.MILLISECONDS)
  @Operation(summary = "Book Queue service ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = User.class)))
  @APIResponse(responseCode = "204", description = "No User with that username found")
  public Response bookQueue(@Valid BookQueue bookQueue) throws InvalidKeySpecException {
    return Response.ok(bookQueueService.bookMyQueue(bookQueue)).build();
  }
}
