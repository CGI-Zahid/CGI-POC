package com.cgi.poc.dw.rest.resource;

import com.cgi.poc.dw.rest.model.UserRegistrationDto;
import com.cgi.poc.dw.service.UserRegistrationService;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/register", basePath = "/")
public class UserRegistrationResource {

  private UserRegistrationService userRegistrationService;
  
  private String apiUrl;

  @Inject
  public UserRegistrationResource(@Named("apiUrl") String apiUrl, UserRegistrationService userRegistrationService) {
    this.apiUrl = apiUrl;
    this.userRegistrationService = userRegistrationService;
  }

  @POST
  @ApiOperation(value = "User profile registration",
      notes = "Allows a user to register.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "System Error")
  })
  public Response signup(@NotNull UserRegistrationDto userRegistrationDto) {

    if (!isValid(userRegistrationDto.getPhone(), "\\d{10}")) {
      throw new BadRequestException("Invalid phone number.");
    }

    if (!isValid(userRegistrationDto.getZipCode(), "\\d{5}")) {
      throw new BadRequestException("Invalid zip code.");
    }

    return userRegistrationService.registerUser(userRegistrationDto);
  }

  private boolean isValid(String input, String regex) {
    if (input != null && input.matches(regex)) {
      return true;
    }
    return false;
  }
}
