/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dawna.floyd
 */
public class EventEarthquakePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "eqid")
    private String eqid;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;

    public EventEarthquakePK() {
    }

    public EventEarthquakePK(String eqid, Date datetime) {
        this.eqid = eqid;
        this.datetime = datetime;
    }

     public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }
     public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eqid != null ? eqid.hashCode() : 0);
        hash += (datetime != null ? datetime.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventEarthquakePK)) {
            return false;
        }
        EventEarthquakePK other = (EventEarthquakePK) object;
        if ((this.eqid == null && other.eqid != null) || (this.eqid != null && !this.eqid.equals(other.eqid))) {
            return false;
        }
        if ((this.datetime == null && other.datetime != null) || (this.datetime != null && !this.datetime.equals(other.datetime))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventEarthquakePK[ eqid=" + eqid + ", datetime=" + datetime + " ]";
    }
    
}
