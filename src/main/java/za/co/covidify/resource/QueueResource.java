package za.co.covidify.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import za.co.covidify.model.Queue;
import za.co.covidify.request.to.BookQueueRQ;
import za.co.covidify.request.to.CancelQueueRQ;
import za.co.covidify.response.to.BookQueueRs;
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
    Queue queue = queueService.findQueueById(id);
    if (queue != null) {
      LOGGER.debug("Found Queue " + queue);
      return Response.ok(queue).build();
    }
    else {
      LOGGER.debug("No Queue found with id " + id);
      return Response.noContent().build();
    }
  }

  @POST
  @Operation(summary = "Create a new Queue ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Queue.class)))
  @Counted(name = "countCreateQueue", description = "Counts how many times the createQueue method has been invoked")
  @Timed(name = "timeGetCreateQueue", description = "Times how long it takes to invoke the createQueue method", unit = MetricUnits.MILLISECONDS)
  public Response createQueue(Queue queue) {
    return Response.ok(queueService.createQueue(queue)).status(201).build();
  }

  @PUT
  public Response updateQueue(Queue queue) {
    return Response.ok(queueService.updateQueue(queue)).status(200).build();
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
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BookQueueRs.class)))
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
