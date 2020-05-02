package za.co.covidify.model.user;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import org.jboss.logging.Logger;

@Path("/api/v1/user")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class SysUserResource {
  private static final Logger LOGGER = Logger.getLogger(SysUserResource.class);

  @Inject
  SysUserService userService;

  @Operation(summary = "Returns all the heroes from the database")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SysUser.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "No heroes")
  @Counted(name = "countGetAllHeroes", description = "Counts how many times the getAllHeroes method has been invoked")
  @Timed(name = "timeGetAllHeroes", description = "Times how long it takes to invoke the getAllHeroes method", unit = MetricUnits.MILLISECONDS)

  @GET
  public List<SysUser> getAllUsers() {
    List<SysUser> users = userService.findAllUsers();
    LOGGER.info("Total number of fights " + users);
    return users;
  }

  @Operation(summary = "Returns a hero for a given identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SysUser.class)))
  @APIResponse(responseCode = "204", description = "The hero is not found for a given identifier")
  @Counted(name = "countGetHero", description = "Counts how many times the getHero method has been invoked")
  @Timed(name = "timeGetHero", description = "Times how long it takes to invoke the getHero method", unit = MetricUnits.MILLISECONDS)
  @GET
  @Path("/{id}")
  public Response getHero(@Parameter(description = "Hero identifier", required = true) @PathParam("id") Long id) {
    SysUser user = userService.findUserById(id);
    if (user != null) {
      LOGGER.debug("Found hero " + user);
      return Response.ok(user).build();
    }
    else {
      LOGGER.debug("No hero found with id " + id);
      return Response.noContent().build();
    }
  }

  @Provider
  public static class ErrorMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
      int code = 500;
      if (exception instanceof WebApplicationException) {
        code = ((WebApplicationException) exception).getResponse().getStatus();
      }
      return Response.status(code).entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build()).build();
    }

  }
}