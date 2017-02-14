package com.cgi.poc.dw.auth.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface PasswordHash {

  /**
   * Returns a salted PBKDF2 hash of the password.
   *
   * @param password the password to hash
   * @return a salted PBKDF2 hash of the password
   * @throws NoSuchAlgorithmException the no such algorithm exception
   * @throws InvalidKeySpecException the invalid key spec exception
   */
  String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException;

  /**
   * Returns a salted PBKDF2 hash of the password.
   *
   * @param password the password to hash
   * @return a salted PBKDF2 hash of the password
   */
  String createHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException;

  /**
   * Validates a password using a hash.
   *
   * @param password the password to check
   * @param correctHash the hash of the valid password
   * @return true if the password is correct, false if not
   * @throws NoSuchAlgorithmException the no such algorithm exception
   * @throws InvalidKeySpecException the invalid key spec exception
   */
  boolean validatePassword(String password, String correctHash)
      throws NoSuchAlgorithmException,
      InvalidKeySpecException;

  /**
   * Validates a password using a hash.
   *
   * @param password the password to check
   * @param correctHash the hash of the valid password
   * @return true if the password is correct, false if not
   * @throws NoSuchAlgorithmException the no such algorithm exception
   * @throws InvalidKeySpecException the invalid key spec exception
   */
  boolean validatePassword(char[] password, String correctHash)
      throws NoSuchAlgorithmException,
      InvalidKeySpecException;
}
