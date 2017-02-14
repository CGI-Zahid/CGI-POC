package com.cgi.poc.dw.auth.service;

import com.cgi.poc.dw.CgiPocConfiguration;
import com.cgi.poc.dw.auth.model.Keys;

import java.security.NoSuchAlgorithmException;
import org.jose4j.keys.HmacKey;

public class KeyBuilderServiceImpl implements KeyBuilderService {

  public Keys createKeys(CgiPocConfiguration configuration) throws NoSuchAlgorithmException {
    Keys keys = new Keys();
    HmacKey signatureKey = createSignatureKey(configuration.getJwtSignatureSecret());
    keys.setSignatureKey(signatureKey);
    return keys;
  }

  public HmacKey createSignatureKey(String signatureSecret) {
    return new HmacKey(signatureSecret.getBytes());
  }
}
