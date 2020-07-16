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

import za.co.covidify.request.to.CancelQueueHeaderRQ;
import za.co.covidify.request.to.CreateQueueHeaderRQ;
import za.co.covidify.response.to.CompanyQueueRS;
import za.co.covidify.response.to.QueueHeaderRS;
import za.co.covidify.services.QueueHeaderService;

@Path("/api/v1/queueheader")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "QueueHeader Api", description = "QueueHeader details operations.")
public class QueueHeaderResource {
  private static final Logger LOGGER = Logger.getLogger(QueueHeaderResource.class);

  @Inject
  QueueHeaderService queueHeaderService;

  @GET
  @Operation(summary = "Returns all the QueueHeaders from the database")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = QueueHeaderRS.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "No QueueHeaders found")
  @Counted(name = "countGetAllQueueHeaders", description = "Counts how many times the getAllQueueHeader method has been invoked")
  @Timed(name = "timeGetAllQueueHeaders", description = "Times how long it takes to invoke the getAllQueueHeaders method", unit = MetricUnits.MILLISECONDS)
  public Response getAllQueueHeaders() {
    return Response.ok(queueHeaderService.findAllQueueHeader()).build();
  }

  @GET
  @Path("/company/{id}")
  @Operation(summary = "Returns all the QueueHeaders from the database")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CompanyQueueRS.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "No QueueHeaders found")
  @Counted(name = "countGetAllQueueHeadersByCo", description = "Counts how many times the getAllQueueHeader method has been invoked")
  @Timed(name = "timeGetAllQueueHeadersByCo", description = "Times how long it takes to invoke the getAllQueueHeaders method", unit = MetricUnits.MILLISECONDS)
  public Response getAllQueueHeadersByCompany(@Parameter(description = "QueueHeader identifier", required = true) @PathParam("id") Long id) {
    return Response.ok(queueHeaderService.findQueueHeaderByCompnayId(id)).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Returns a QueueHeader for a given identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = QueueHeaderRS.class)))
  @APIResponse(responseCode = "204", description = "The QueueHeader is not found for a given identifier")
  @Counted(name = "countGetQueueHeader", description = "Counts how many times the getQueueHeader method has been invoked")
  @Timed(name = "timeGetQueueHeader", description = "Times how long it takes to invoke the getQueueHeader method", unit = MetricUnits.MILLISECONDS)
  public Response getQueueHeader(@Parameter(description = "QueueHeader identifier", required = true) @PathParam("id") Long id) {
    QueueHeaderRS queueHeader = queueHeaderService.findQueueHeaderById(id);
    if (queueHeader != null) {
      LOGGER.debug("Found QueueHeader " + queueHeader);
      return Response.ok(queueHeader).build();
    }
    else {
      LOGGER.debug("No QueueHeader found with id " + id);
      return Response.noContent().build();
    }
  }

  @POST
  @Operation(summary = "Create a new QueueHeader ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreateQueueHeaderRQ.class)))
  @Counted(name = "countCreateQueueHeader", description = "Counts how many times the createQueueHeader method has been invoked")
  @Timed(name = "timeGetCreateQueueHeader", description = "Times how long it takes to invoke the createQueueHeader method", unit = MetricUnits.MILLISECONDS)
  public Response createQueueHeader(@Valid CreateQueueHeaderRQ queueHeaderRq) {
    return Response.ok(queueHeaderService.createQueueHeader(queueHeaderRq)).status(201).build();
  }

  @PUT
  @Path("/company/cancel")
  @Counted(name = "countPut_CancelQueueHeader", description = "How many calls have been performed")
  @Timed(name = "timePut_CancelQueueHeader", description = "How long it takes to perform check.", unit = MetricUnits.MILLISECONDS)
  @Operation(summary = "Cancel Queue Header service ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @APIResponse(responseCode = "400", description = "The request has not been applied because of invalid input parameters.", content = @Content(mediaType = "application/json"))
  @APIResponse(responseCode = "500", description = "Internal Server Error. The service call has not succeeded. The string in the body may contain the details.", content = @Content(mediaType = "application/json"))
  public Response cancleQueue(@Valid CancelQueueHeaderRQ cancelQueueRQ) {
    queueHeaderService.cancelQueueHeader(cancelQueueRQ);
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
