package za.co.covidify.model.user;

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

@Path("/api/v1/user")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "User Api", description = "User details operations.")
public class SysUserResource {
  private static final Logger LOGGER = Logger.getLogger(SysUserResource.class);

  @Inject
  SysUserService userService;

  @GET
  @Operation(summary = "Returns all the users from the database")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SysUser.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "No users found")
  @Counted(name = "countGetAllUsers", description = "Counts how many times the getAllUsers method has been invoked")
  @Timed(name = "timeGetAllUsers", description = "Times how long it takes to invoke the getAllUsers method", unit = MetricUnits.MILLISECONDS)
  public List<SysUser> getAllUsers() {
    List<SysUser> users = userService.findAllUsers();
    LOGGER.info("Total number of users " + users);
    return users;
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Returns a user for a given identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SysUser.class)))
  @APIResponse(responseCode = "204", description = "The user is not found for a given identifier")
  @Counted(name = "countGetUser", description = "Counts how many times the getUser method has been invoked")
  @Timed(name = "timeGetUser", description = "Times how long it takes to invoke the getUser method", unit = MetricUnits.MILLISECONDS)
  public Response getUser(@Parameter(description = "User identifier", required = true) @PathParam("id") Long id) {
    SysUser user = userService.findUserById(id);
    if (user != null) {
      LOGGER.debug("Found user " + user);
      return Response.ok(user).build();
    }
    else {
      LOGGER.debug("No user found with id " + id);
      return Response.noContent().build();
    }
  }

  @POST
  @Operation(summary = "Create a new user ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SysUser.class)))
  @Counted(name = "countCreateUser", description = "Counts how many times the createUser method has been invoked")
  @Timed(name = "timeGetCreateUser", description = "Times how long it takes to invoke the createUser method", unit = MetricUnits.MILLISECONDS)
  public Response createUser(SysUser user) {
    user = userService.createUser(user);
    return Response.ok(user).status(201).build();
  }

  @PUT
  public Response updateUser(SysUser user) {
    user = userService.updateUser(user);
    return Response.ok(user).status(200).build();
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