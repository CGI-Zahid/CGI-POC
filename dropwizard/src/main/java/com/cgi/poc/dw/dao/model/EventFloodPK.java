/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dawna.floyd
 */
public class EventFloodPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "waterbody")
    private String waterbody;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 26)
    @Column(name = "obstime")
    private String obstime;

    public EventFloodPK() {
    }

    public EventFloodPK(String waterbody, String obstime) {
        this.waterbody = waterbody;
        this.obstime = obstime;
    }

    public String getWaterbody() {
        return waterbody;
    }

    public void setWaterbody(String waterbody) {
        this.waterbody = waterbody;
    }

    public String getObstime() {
        return obstime;
    }

    public void setObstime(String obstime) {
        this.obstime = obstime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (waterbody != null ? waterbody.hashCode() : 0);
        hash += (obstime != null ? obstime.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventFloodPK)) {
            return false;
        }
        EventFloodPK other = (EventFloodPK) object;
        if ((this.waterbody == null && other.waterbody != null) || (this.waterbody != null && !this.waterbody.equals(other.waterbody))) {
            return false;
        }
        if ((this.obstime == null && other.obstime != null) || (this.obstime != null && !this.obstime.equals(other.obstime))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventFloodPK[ waterbody=" + waterbody + ", obstime=" + obstime + " ]";
    }
    
}
