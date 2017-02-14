package com.cgi.poc.dw.service;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.auth.service.JwtBuilderService;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.rest.model.AuthTokenResponseDto;
import com.cgi.poc.dw.util.LoginValidationGroup;
import com.google.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

  private final static Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);
  private final UserDao userDao;
  private final JwtBuilderService jwtBuilderService;
  private final PasswordHash passwordHash;

  @Inject
  public LoginServiceImpl(UserDao userDao, JwtBuilderService jwtBuilderService,
      PasswordHash passwordHash, Validator validator) {
    super(validator);
    this.userDao = userDao;
    this.jwtBuilderService = jwtBuilderService;
    this.passwordHash = passwordHash;
  }

  @Override
  public Response login(User user) {

   if (user == null) {
      throw new BadRequestException("Missing credentials.");
    }
    
    validate(user, "rest", LoginValidationGroup.class);

    User retUser = userDao.findUserByEmail(user.getEmail());
    if (retUser == null) {
      LOG.warn("User not found.");
      throw new NotFoundException("Invalid username or password.");
    }
    
    
    if (hasValidPassword(user, retUser)) {
      String authToken;
      try {
        authToken = jwtBuilderService.createJwt(user);
      } catch (JoseException e) {
        LOG.error("Unable to create authToken.", e);
        throw new InternalServerErrorException("Unable to issue authToken.");
      }
      AuthTokenResponseDto authTokenResponseDto = new AuthTokenResponseDto(authToken,
          Role.valueOf(retUser.getRole()));
      return Response.ok().entity(authTokenResponseDto).build();
    } else {
      LOG.warn("Invalid password.");
      throw new NotFoundException("Invalid username or password.");
    }
  }

  private boolean hasValidPassword(User loginUserDto, User user) {
    boolean hasValidPassword = false;
    try {
      hasValidPassword = passwordHash
          .validatePassword(loginUserDto.getPassword(), user.getPassword());
    } catch (Exception e) {
      LOG.error("Unable to compute password hash.", e);
      throw new InternalServerErrorException("Unable to compute password hash.");
    }
    return hasValidPassword;
  }
}
