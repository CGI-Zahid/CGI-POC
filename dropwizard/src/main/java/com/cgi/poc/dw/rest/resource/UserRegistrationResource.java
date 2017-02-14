package com.cgi.poc.dw.rest.resource;

import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.service.UserRegistrationServiceImpl;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cgi.poc.dw.service.UserRegistrationService;
import com.google.inject.Inject;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/register", basePath = "/")
public class UserRegistrationResource{

  private final static Logger LOG = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);
  
  @Inject
  UserRegistrationService userRegistrationService;

  @POST
  @UnitOfWork
  @ApiOperation(value = "User profile registration",
      notes = "Allows a user to register.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "System Error")
  })
  @Timed(name = "User.create")
  public Response signup( @NotNull User user) {
        return userRegistrationService.registerUser(user);
  }
}
