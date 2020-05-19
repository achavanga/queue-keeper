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

import za.co.covidify.model.Person;
import za.co.covidify.services.PersonService;

@Path("/api/v1/Person")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Person Api", description = "Person details operations.")
public class PersonResource {
  private static final Logger LOGGER = Logger.getLogger(PersonResource.class);

  @Inject
  PersonService personService;

  @GET
  @Operation(summary = "Returns all the Persons from the database")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Person.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "No Persons found")
  @Counted(name = "countGetAllPersons", description = "Counts how many times the getAllPersons method has been invoked")
  @Timed(name = "timeGetAllPersons", description = "Times how long it takes to invoke the getAllPersons method", unit = MetricUnits.MILLISECONDS)
  public List<Person> getAllPersons() {
    List<Person> persons = personService.findAllPersons();
    LOGGER.info("Total number of Persons " + persons.size());
    return persons;
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Returns a Person for a given identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Person.class)))
  @APIResponse(responseCode = "204", description = "The Person is not found for a given identifier")
  @Counted(name = "countGetPerson", description = "Counts how many times the getPerson method has been invoked")
  @Timed(name = "timeGetPerson", description = "Times how long it takes to invoke the getPerson method", unit = MetricUnits.MILLISECONDS)
  public Response getPerson(@Parameter(description = "Person identifier", required = true) @PathParam("id") Long id) {
    Person person = personService.findPersonById(id);
    if (person != null) {
      LOGGER.debug("Found Person " + person);
      return Response.ok(person).build();
    }
    else {
      LOGGER.debug("No Person found with id " + id);
      return Response.noContent().build();
    }
  }

  @POST
  @Operation(summary = "Create a new Person ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Person.class)))
  @Counted(name = "countCreatePerson", description = "Counts how many times the createPerson method has been invoked")
  @Timed(name = "timeGetCreatePerson", description = "Times how long it takes to invoke the createPerson method", unit = MetricUnits.MILLISECONDS)
  public Response createPerson(Person person) {

    return Response.ok(personService.createPerson(person)).status(201).build();
  }

  @PUT
  public Response updatePerson(Person person) {
    person = personService.updatePerson(person);
    return Response.ok(person).status(200).build();
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
