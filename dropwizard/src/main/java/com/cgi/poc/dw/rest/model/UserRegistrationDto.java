package com.cgi.poc.dw.rest.model;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.dao.model.NotificationType;
import java.io.Serializable;
import java.util.List;

public class UserRegistrationDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String firstName;

  private String lastName;

  private String email;

  private String password;

  private String phone;

  private String zipCode;

  private Role role;

  private List<NotificationType> notificationType;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public List<NotificationType> getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(
      List<NotificationType> notificationType) {
    this.notificationType = notificationType;
  }
}
