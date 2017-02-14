package com.cgi.poc.dw.rest.resource;

import com.cgi.poc.dw.auth.service.JwtBuilderService;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.service.LoginService;
import com.cgi.poc.dw.service.LoginServiceImpl;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/login", basePath = "/")
public class LoginResource {
  @Inject
  private LoginService loginService;
  


  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "User login",
      notes = "Allows the user to login by returning a JWT token on success")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 401, message = "Authentication failed."),
      @ApiResponse(code = 500, message = "System Error")
  })
  @UnitOfWork
  @Timed(name = "User.login")
    public Response login(User user) {
        return loginService.login(user);

    }
}
