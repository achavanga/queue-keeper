package za.co.covidify.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import za.co.covidify.model.Queue;
import za.co.covidify.request.to.BookQueueForOtherRQ;
import za.co.covidify.request.to.BookQueueRQ;
import za.co.covidify.request.to.CancelQueueRQ;
import za.co.covidify.response.to.BookQueueRs;
import za.co.covidify.response.to.PersonQueueRS;
import za.co.covidify.response.to.QueueRS;
import za.co.covidify.services.QueueService;

@Path("/api/v1/queue")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Queue Api", description = "Queue details operations.")
public class QueueResource {
  private static final Logger LOGGER = Logger.getLogger(QueueResource.class);

  @Inject
  QueueService queueService;

  @GET
  @Operation(summary = "Returns all the Queues from the database")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Queue.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "No Queues found")
  @Counted(name = "countGetAllQueues", description = "Counts how many times the getAllQueue method has been invoked")
  @Timed(name = "timeGetAllQueues", description = "Times how long it takes to invoke the getAllQueues method", unit = MetricUnits.MILLISECONDS)
  public Response getAllQueues() {
    return Response.ok(queueService.findAllQueue()).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Returns a Queue for a given identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Queue.class)))
  @APIResponse(responseCode = "204", description = "The Queue is not found for a given identifier")
  @Counted(name = "countGetQueue", description = "Counts how many times the getQueue method has been invoked")
  @Timed(name = "timeGetQueue", description = "Times how long it takes to invoke the getQueue method", unit = MetricUnits.MILLISECONDS)
  public Response getQueue(@Parameter(description = "Queue identifier", required = true) @PathParam("id") Long id) {
    QueueRS queue = queueService.findQueueById(id);
    if (queue != null) {
      LOGGER.debug("Found Queue " + queue);
      return Response.ok(queue).build();
    }
    else {
      LOGGER.debug("No Queue found with id " + id);
      return Response.noContent().build();
    }
  }

  @GET
  @Path("/person/{personId}")
  @Operation(summary = "Returns a Person Queues for a given Person identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PersonQueueRS.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "The Person Queue is not found for a given identifier")
  @Counted(name = "countGetPersonQueue", description = "Counts how many times the getPersonQueue method has been invoked")
  @Timed(name = "timeGetPersonQueue", description = "Times how long it takes to invoke the getPersonQueue method", unit = MetricUnits.MILLISECONDS)
  public Response getPersonQueue(@Parameter(description = "Person identifier", required = true) @PathParam("personId") Long personId) {
    return Response.ok(queueService.findQueueByPersonId(personId)).build();
  }

  @PUT
  public Response updateQueue(Queue queue) {
    return Response.ok(queueService.updateQueue(queue)).status(200).build();
  }

  @POST
  @Path("/book/other")
  @Counted(name = "countPost_BookQueueForOther", description = "How many calls have been performed")
  @Timed(name = "timePost_BookQueueForOther", description = "How long it takes to perform check.", unit = MetricUnits.MILLISECONDS)
  @Operation(summary = "Book Queue for Others service ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BookQueueRs.class)))
  public Response bookQueueForOtherRQ(@Valid BookQueueForOtherRQ bookQueueForOtherRQ) {
    return Response.ok(queueService.bookQueueForOther(bookQueueForOtherRQ)).build();
  }

  @POST
  @Path("/book")
  @Counted(name = "countPost_BookQueue", description = "How many calls have been performed")
  @Timed(name = "timePost_BookQueue", description = "How long it takes to perform check.", unit = MetricUnits.MILLISECONDS)
  @Operation(summary = "Book Queue service ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BookQueueRs.class)))
  public Response bookQueueRQ(@Valid BookQueueRQ bookQueueRQ) {
    return Response.ok(queueService.bookMyQueue(bookQueueRQ)).build();
  }

  @PUT
  @Path("/cancel")
  @Counted(name = "countPut_CancelQueue", description = "How many calls have been performed")
  @Timed(name = "timePut_CancelQueue", description = "How long it takes to perform check.", unit = MetricUnits.MILLISECONDS)
  @Operation(summary = "Cancel Queue service ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON))
  public Response cancleQueue(@Valid CancelQueueRQ cancelQueueRQ) {
    queueService.cancelMyQueue(cancelQueueRQ);
    return Response.ok().build();
  }

  @PUT
  @Path("/confirm")
  @Counted(name = "countPut_ConfirmQueue", description = "How many calls have been performed")
  @Timed(name = "timePut_ConfirmQueue", description = "How long it takes to perform check.", unit = MetricUnits.MILLISECONDS)
  @Operation(summary = "Confirm Queue service ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BookQueueRs.class)))
  public Response confirmQueue(@Valid CancelQueueRQ cancelQueueRQ) {
    queueService.confirmMyQueue(cancelQueueRQ);
    return Response.ok().build();
  }

  // verifyMyQueue
  @GET
  @Path("/verify/")
  @Operation(summary = "Verify Queues by queue id and company id ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)))
  @APIResponse(responseCode = "204", description = "The Queue is not found for a given identifier")
  @APIResponse(responseCode = "400", description = "The request has not been applied because of invalid input parameters.", content = @Content(mediaType = "application/json"))
  @APIResponse(responseCode = "401", description = "The request has not been applied because it lacks valid authentication credentials for the target resource.", content = @Content(mediaType = "application/json"))
  @APIResponse(responseCode = "500", description = "Internal Server Error. The service call has not succeeded. The string in the body may contain the details.", content = @Content(mediaType = "application/json"))
  @Counted(name = "countVerfiyQueue", description = "Counts how many times the Verify Queue method has been invoked")
  @Timed(name = "timeVerifyQueue", description = "Times how long it takes to invoke the Verify Queue method", unit = MetricUnits.MILLISECONDS)
  public Response verifyMyQueue(@QueryParam("companyId") @NotNull(message = "Company Id cannot be null") Long companyId,
      @QueryParam("queueId") @NotNull(message = "Queue Id cannot be null") Long queueId) {
    return queueService.verifyMyQueue(companyId, queueId);
  }

  // getQueueReadyForService
  @GET
  @Path("/nextInQueue/")
  @Operation(summary = "Next In Queues by size ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Queue.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "The Queue is not found for a given identifier")
  @APIResponse(responseCode = "400", description = "The request has not been applied because of invalid input parameters.", content = @Content(mediaType = "application/json"))
  @APIResponse(responseCode = "401", description = "The request has not been applied because it lacks valid authentication credentials for the target resource.", content = @Content(mediaType = "application/json"))
  @APIResponse(responseCode = "500", description = "Internal Server Error. The service call has not succeeded. The string in the body may contain the details.", content = @Content(mediaType = "application/json"))
  @Counted(name = "countnextInQueue", description = "Counts how many times the Verify Queue method has been invoked")
  @Timed(name = "timenextInQueue", description = "Times how long it takes to invoke the Verify Queue method", unit = MetricUnits.MILLISECONDS)
  public Response nextInQueue(@QueryParam("size") @NotNull(message = "Size cannot be null") int size) {
    return Response.ok(queueService.getQueueReadyForService(size)).build();
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
