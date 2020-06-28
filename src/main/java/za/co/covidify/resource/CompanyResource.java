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

import za.co.covidify.model.Company;
import za.co.covidify.response.to.CompanyRS;
import za.co.covidify.services.CompanyService;

@Path("/api/v1/company")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Company Api", description = "Company details operations.")
public class CompanyResource {
  private static final Logger LOGGER = Logger.getLogger(CompanyResource.class);

  @Inject
  CompanyService companyService;

  @GET
  @Operation(summary = "Returns all the Companys from the database")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Company.class, type = SchemaType.ARRAY)))
  @APIResponse(responseCode = "204", description = "No Companys found")
  @Counted(name = "countGetAllCompanys", description = "Counts how many times the getAllCompany method has been invoked")
  @Timed(name = "timeGetAllCompanys", description = "Times how long it takes to invoke the getAllCompanys method", unit = MetricUnits.MILLISECONDS)
  public Response getAllCompanys() {
    return Response.ok(companyService.findAllCompany()).build();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Returns a Company for a given identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CompanyRS.class)))
  @APIResponse(responseCode = "204", description = "The Company is not found for a given identifier")
  @Counted(name = "countGetCompany", description = "Counts how many times the getCompany method has been invoked")
  @Timed(name = "timeGetCompany", description = "Times how long it takes to invoke the getCompany method", unit = MetricUnits.MILLISECONDS)
  public Response getCompany(@Parameter(description = "Company identifier", required = true) @PathParam("id") Long id) {
    CompanyRS company = companyService.findCompanyById(id);
    if (company != null) {
      LOGGER.debug("Found Company " + company);
      return Response.ok(company).build();
    }
    else {
      LOGGER.debug("No Company found with id " + id);
      return Response.noContent().build();
    }
  }

  @GET
  @Path("/search/{name}")
  @Operation(summary = "Returns List Company for a given identifier")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CompanyRS.class)))
  @APIResponse(responseCode = "204", description = "The Company is not found for a given identifier")
  @Counted(name = "countGetCompanyByName", description = "Counts how many times the getCompany method has been invoked")
  @Timed(name = "timeGetCompanyByName", description = "Times how long it takes to invoke the getCompany method", unit = MetricUnits.MILLISECONDS)
  public Response getCompanyByName(@Parameter(description = "Company identifier", required = true) @PathParam("name") String name) {

    return Response.ok(companyService.findCompanyByName(name)).build();
  }

  @POST
  @Operation(summary = "Create a new Company ")
  @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CompanyRS.class)))
  @Counted(name = "countCreateCompany", description = "Counts how many times the createCompany method has been invoked")
  @Timed(name = "timeGetCreateCompany", description = "Times how long it takes to invoke the createCompany method", unit = MetricUnits.MILLISECONDS)
  public Response createCompany(Company company) {
    return Response.ok(companyService.createCompany(company)).status(201).build();
  }

  @PUT
  public Response updateCompany(Company company) {
    int i = companyService.updateCompany(company);
    if (i > 0) {
      return Response.ok().status(200).build();
    }
    else {
      throw new WebApplicationException("Company with id of " + company.id + " does not exist.", 404);
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
      return Response.status(code).entity(Json.createObjectBuilder().add("Error ", exception.getMessage()).add("Code ", code).build()).build();
    }

  }
}
