package com.cgi.poc.dw;

public class MailConfiguration
{
  private String hostname;

  private int port;

  private boolean disableSend;

  private boolean debug;

  private boolean ssl;

  private boolean tls;

  private String username;

  private String password;

  private String systemEmail;

  private String systemPersonal;
  
  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getHostname() {
    return hostname;
  }

  public int getPort() {
    return port;
  }

  public boolean isDisableSend() {
    return disableSend;
  }

  public boolean isDebug() {
    return debug;
  }

  public boolean isSsl() {
    return ssl;
  }

  public boolean isTls() {
    return tls;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getSystemEmail() {
    return systemEmail;
  }

  public void setSystemEmail(String systemEmail) {
    this.systemEmail = systemEmail;
  }

  public String getSystemPersonal() {
    return systemPersonal;
  }

  public void setSystemPersonal(String systemPersonal) {
    this.systemPersonal = systemPersonal;
  }
}
