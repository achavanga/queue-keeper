package za.co.covidify.resource;

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

import za.co.covidify.model.User;
import za.co.covidify.services.UserService;

@Path("/api/v1/user")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "User Api", description = "User details operations.")
public class UserResource {
  private static final Logger LOGGER = Logger.getLogger(UserResource.class);

  @Inject
  UserService userService;

  @GET
  @Operation(summary = "Returns all the users from the database")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "No users found")
  @Counted(name = "countGetAllUsers", description = "Counts how many times the getAllUsers method has been invoked")
  @Timed(name = "timeGetAllUsers", description = "Times how long it takes to invoke the getAllUsers method", unit = MetricUnits.MILLISECONDS)
  public Response getAllUsers() {
    return Response.ok(userService.findAllUsers()).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Returns a user for a given identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = User.class)))
  @APIResponse(responseCode = "204", description = "The user is not found for a given identifier")
  @Counted(name = "countGetUser", description = "Counts how many times the getUser method has been invoked")
  @Timed(name = "timeGetUser", description = "Times how long it takes to invoke the getUser method", unit = MetricUnits.MILLISECONDS)
  public Response getUser(@Parameter(description = "User identifier", required = true) @PathParam("id") Long id) {
    User user = userService.findUserById(id);
    if (user != null) {
      LOGGER.debug("Found user " + user);
      return Response.ok(user).build();
    }
    else {
      LOGGER.debug("No user found with id " + id);
      return Response.noContent().build();
    }
  }

  @GET
  @Path("/{username}")
  // throw new PersonException("Person cannot be emprty");
  @Operation(summary = "Returns a user for a given user name")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = User.class)))
  @APIResponse(responseCode = "204", description = "The user is not found for a given username")
  @Counted(name = "countGetUserByName", description = "Counts how many times the getUserByName method has been invoked")
  @Timed(name = "timeGetUserByName", description = "Times how long it takes to invoke the getUserByName method", unit = MetricUnits.MILLISECONDS)
  public Response getUserByName(@Parameter(description = "Username", required = true) @PathParam("username") String username) {
    User user = userService.findByName(username);
    if (user != null) {
      LOGGER.debug("Found user " + user);
      return Response.ok(user).build();
    }
    else {
      LOGGER.debug("No user found with user name " + username);
      return Response.noContent().build();
    }
  }

  @POST
  @Operation(summary = "Create a new user ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = User.class)))
  @Counted(name = "countCreateUser", description = "Counts how many times the createUser method has been invoked")
  @Timed(name = "timeGetCreateUser", description = "Times how long it takes to invoke the createUser method", unit = MetricUnits.MILLISECONDS)
  public Response createUser(User user) {
    return Response.ok(userService.createUser(user)).status(201).build();
  }

  @PUT
  public Response updateUser(User user) {
    return Response.ok(userService.updateUser(user)).status(200).build();
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