package com.cgi.poc.dw.dao.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
  EMAIL(1), SMS(2), PUSH(3);

  @JsonCreator
  NotificationType(Integer value) {
    this.value = value;
  }

  @JsonValue
  public Integer getValue() { return value; }
  private Integer value;
}
