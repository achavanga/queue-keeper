package za.co.covidify.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
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

import za.co.covidify.model.Address;
import za.co.covidify.services.AddressService;

@Path("/api/v1/address")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Address Api", description = "Address details operations.")
public class AddressResource {
  private static final Logger LOGGER = Logger.getLogger(AddressResource.class);

  @Inject
  AddressService addressService;

  @GET
  @Operation(summary = "Returns all the Addresss from the database")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Address.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "No Addresss found")
  @Counted(name = "countGetAllAddresss", description = "Counts how many times the getAllAddress method has been invoked")
  @Timed(name = "timeGetAllAddresss", description = "Times how long it takes to invoke the getAllAddresss method", unit = MetricUnits.MILLISECONDS)
  public List<Address> getAllAddresss() {
    List<Address> address = addressService.findAllAddress();
    LOGGER.info("Total number of Addresss " + address.size());
    return address;
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Returns a Address for a given identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Address.class)))
  @APIResponse(responseCode = "204", description = "The Address is not found for a given identifier")
  @Counted(name = "countGetAddress", description = "Counts how many times the getAddress method has been invoked")
  @Timed(name = "timeGetAddress", description = "Times how long it takes to invoke the getAddress method", unit = MetricUnits.MILLISECONDS)
  public Response getAddress(@Parameter(description = "Address identifier", required = true) @PathParam("id") Long id) {
    Address address = addressService.findAddressById(id);
    if (address != null) {
      LOGGER.debug("Found Address " + address);
      return Response.ok(address).build();
    }
    else {
      LOGGER.debug("No Address found with id " + id);
      return Response.noContent().build();
    }
  }

  @POST
  @Operation(summary = "Create a new Address ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Address.class)))
  @APIResponse(responseCode = "201", description = "The Address created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Address.class)))
  @Counted(name = "countCreateAddress", description = "Counts how many times the createAddress method has been invoked")
  @Timed(name = "timeGetCreateAddress", description = "Times how long it takes to invoke the createAddress method", unit = MetricUnits.MILLISECONDS)
  public Response createAddress(Address address) {
    address = addressService.createAddress(address);
    return Response.ok(address).status(201).build();
  }

  @PUT
  public Response updateAddress(Address address) {
    address = addressService.updateAddress(address);
    return Response.ok(address).status(200).build();
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
