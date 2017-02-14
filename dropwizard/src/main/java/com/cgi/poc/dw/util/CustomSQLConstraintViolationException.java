/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.validation.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;

/**
 *
 * @author dawna.floyd
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class CustomSQLConstraintViolationException   implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Response response;
        ErrorInfo errRet = new ErrorInfo();
        String errorString = GeneralErrors.CONSTRAINT_VIOLATION.getMessage().replace("REPLACE",  exception.getConstraintName());
        errRet.addError(GeneralErrors.CONSTRAINT_VIOLATION.getCode(), errorString + ": " + exception.getSQLException().getMessage());
        
        response = Response.noContent().status(Response.Status.BAD_REQUEST).entity(errRet).build();

        return response;
    }

}