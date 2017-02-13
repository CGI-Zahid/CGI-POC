package com.cgi.poc.dw.rest.model;


import com.cgi.poc.dw.auth.model.Role;

public class AuthTokenResponseDto {

  private String authToken;
  private Role role = Role.RESIDENT;

  public AuthTokenResponseDto() {
  }

  public AuthTokenResponseDto(String authToken, Role role) {
    this.authToken = authToken;
    this.role = role;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
