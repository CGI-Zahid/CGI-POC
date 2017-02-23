package com.cgi.poc.dw.rest.resource;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.rest.model.LocalizationDto;

import com.cgi.poc.dw.service.UserService;
import com.cgi.poc.dw.service.UserServiceImpl;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


import org.apache.commons.lang3.StringUtils;



@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/user", basePath = "/")
public class UserResource {

	private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	@Inject
	private UserService userService;

	@POST
	@UnitOfWork
	@ApiOperation(value = "User profile registration", notes = "Allows a user to register.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "System Error") })
	@Timed(name = "User.save")
	public Response signup(@NotNull User user) {
		Response response = userService.registerUser(user);
		if (!response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
			throw new WebApplicationException(response);
		}
		return response;
	}

	@PUT
	@UnitOfWork
	@ApiOperation(value = "User profile update", notes = "Allows a user to update.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "System Error") })
	@Timed(name = "User.save")
	public Response updateProfile(@Auth User user, @NotNull User modifiedUser) {
		//If user password is empty keep same password.
		if(StringUtils.isBlank(modifiedUser.getPassword())){
			modifiedUser.setPassword(user.getPassword());
		}
		modifiedUser.setId(user.getId());
		modifiedUser.setRole(user.getRole());
		
		Response response = userService.updateUser(modifiedUser);
		if (!response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
			throw new WebApplicationException(response);
		}
		return response;
	}
	
	  @RolesAllowed("RESIDENT")
	  @Path("/geoLocation")
	  @PUT
	  @UnitOfWork
	  @ApiOperation(value = "User localization",
	      notes = "stores the users current geo location")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Success"),
	      @ApiResponse(code = 500, message = "System Error")
	  })
	  @ApiImplicitParams({
	      @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")
	  })
	  @Timed(name = "User.save")
	  public Response setLocalization(@Auth User user,@NotNull @Valid LocalizationDto localizationDto) {
		  
		  user.setGeoLocLatitude(localizationDto.getLatitude());
		  user.setGeoLocLongitude(localizationDto.getLongitude());
		  
	      Response response = userService.setLocalization(user);
	      if (!response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
	                throw new WebApplicationException(response);
	      }
	    return response;
	  }
}
