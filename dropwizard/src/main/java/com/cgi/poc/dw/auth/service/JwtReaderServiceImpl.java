package com.cgi.poc.dw.auth.service;

import com.cgi.poc.dw.auth.model.Keys;
import com.google.inject.Inject;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtReaderServiceImpl implements JwtReaderService {

  private final static Logger LOG = LoggerFactory.getLogger(JwtReaderServiceImpl.class);
  private final Keys keys;

  @Inject
  public JwtReaderServiceImpl(Keys keys) {
    this.keys = keys;
  }

  public String getEmailFromJwt(String token) throws InvalidJwtException {
    JwtConsumer jwtConsumer = new JwtConsumerBuilder().setEvaluationTime(NumericDate.now())
        .setVerificationKey(keys.getSignatureKey()).build();
    JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
    return (String) jwtClaims.getClaimValue("email");
  }
}
