package com.cgi.poc.dw.service;

public interface FirebasePushService {
  /**
   * Sends a push notification
   *
   * @param token FCM token for user.
   * @param title of the push notification.
   * @param message body of the message for the push notification
   */
  public boolean send(String token, String title, String message);

}
