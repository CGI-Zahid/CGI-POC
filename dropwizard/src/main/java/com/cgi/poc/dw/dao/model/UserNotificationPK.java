/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import java.io.Serializable;

/**
 *
 * @author dawna.floyd
 */

public class UserNotificationPK implements Serializable {
    protected Long notificationId;
    protected User userId;

    public UserNotificationPK() {}

    public UserNotificationPK(Long notificationId, User userId) {
        this.notificationId = notificationId;
        this.userId = userId;
    }
    public int hashCode() {
        int hash = 0;
        hash += (notificationId != null ? notificationId.hashCode() : 0);
        hash += (userId.getId() != null ? userId.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserNotificationPK)) {
            return false;
        }
        UserNotificationPK other = (UserNotificationPK) object;
        if ((this.notificationId == null && other.notificationId != null) || (this.notificationId != null && !this.notificationId.equals(other.notificationId))) {
            return false;
        }
        if ((this.userId.getId() == null && other.userId.getId() != null) || (this.userId.getId() != null && !this.userId.getId().equals(other.userId.getId()))) {
            return false;
        }
        return true;
    }

}