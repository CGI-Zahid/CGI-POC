package com.cgi.poc.dw.rest.model;

import com.cgi.poc.dw.util.LoginValidationGroup;
import com.cgi.poc.dw.util.PasswordType;
import com.cgi.poc.dw.util.RestValidationGroup;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

public class LoginUserDto {

  @ApiModelProperty(value = "Validates for standard email format:  XXX@YYY.ZZZ. No whitespace allowed ", required = true)
  @Pattern(groups = {Default.class,
      LoginValidationGroup.class}, regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Invalid email address.")
  @NotNull(groups = {Default.class, LoginValidationGroup.class})
  @Size(min = 1, max = 150, groups = {Default.class, LoginValidationGroup.class})
  private String email;

  @Basic(optional = false)
  @NotNull(message = "is missing", groups = {Default.class, LoginValidationGroup.class})
  @Size(min = 2, max = 150, message = "must be at least 2 characters in length.")
  @Column(name = "password")
  @PasswordType(message = "must be greater that 2 character, contain no whitespace, and have at least one number and one letter.", groups = {
      RestValidationGroup.class, LoginValidationGroup.class})
  private String password;

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

}
