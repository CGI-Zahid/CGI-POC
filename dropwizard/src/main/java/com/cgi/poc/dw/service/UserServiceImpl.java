package com.cgi.poc.dw.service;

import com.cgi.poc.dw.api.service.MapsApiService;
import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.factory.AddressBuilder;
import com.cgi.poc.dw.rest.dto.FcmTokenDto;
import com.cgi.poc.dw.validator.LoginValidationGroup;
import com.cgi.poc.dw.validator.PersistValidationGroup;
import com.cgi.poc.dw.validator.RestValidationGroup;
import com.cgi.poc.dw.validator.ValidationErrors;
import com.google.inject.Inject;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

	private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserDao userDao;

	private final PasswordHash passwordHash;
	
  private final AddressBuilder addressBuilder;

  private MapsApiService mapsApiService;
	
	@Inject
	public UserServiceImpl(MapsApiService mapsApiService, UserDao userDao, PasswordHash passwordHash,
			Validator validator, AddressBuilder addressBuilder) {
		super(validator);
		this.userDao = userDao;
		this.passwordHash = passwordHash;
		this.mapsApiService = mapsApiService;
    this.addressBuilder = addressBuilder;
  }

	public Response registerUser(User user) {
		// Defaulting the user to RESIDENT
		user.setRole("RESIDENT");

		validate(user, "rest", RestValidationGroup.class, Default.class);
		// check if the email already exists.
		User exitingUser = userDao.findUserByEmail(user.getEmail());
		if (exitingUser != null) {
			throw new BadRequestException(ValidationErrors.DUPLICATE_USER);
		}

		return processForSave(user, false);
	}
	
	public Response updateUser(User user, User modifiedUser) {
		//If user password is empty keep same password.
		boolean keepPassword = false;
		if(StringUtils.isBlank(modifiedUser.getPassword())){
			modifiedUser.setPassword(user.getPassword());
			keepPassword = true;
		}
		else {
			validate(modifiedUser, "update", LoginValidationGroup.class);
		}
		modifiedUser.setId(user.getId());
		modifiedUser.setRole(user.getRole());
		return processForSave(modifiedUser, keepPassword);
	}

	@Override
	public Response updateFcmToken(User user, FcmTokenDto fcmTokenDto) {
		user.setFcmtoken(fcmTokenDto.getFcmtoken());

		validate(user, "save fcm token", Default.class, PersistValidationGroup.class);
		userDao.save(user);

		return Response.ok().build();
	}

	private Response processForSave(User user, boolean keepPassword) {
		Response response = null;
		if (!keepPassword) {
			String hash = passwordHash.createHash(user.getPassword());
			user.setPassword(hash);
		}

		GeoCoordinates geoCoordinates = mapsApiService.getGeoCoordinatesByAddress(addressBuilder.build(user));
		user.setLatitude(geoCoordinates.getLatitude());
		user.setLongitude(geoCoordinates.getLongitude());

		validate(user, "save", Default.class, PersistValidationGroup.class);
		userDao.save(user);

		response = Response.ok().entity(user).build();

		return response;
	}

}
