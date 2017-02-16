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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dawna.floyd
 */
@Embeddable
public class EventHurricanePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "id")
    private String id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "pubdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pubdate;

    public EventHurricanePK() {
    }

    public EventHurricanePK(String id, Date pubdate) {
        this.id = id;
        this.pubdate = pubdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        hash += (pubdate != null ? pubdate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventHurricanePK)) {
            return false;
        }
        EventHurricanePK other = (EventHurricanePK) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if ((this.pubdate == null && other.pubdate != null) || (this.pubdate != null && !this.pubdate.equals(other.pubdate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventHurricanePK[ id=" + id + ", pubdate=" + pubdate + " ]";
    }
    
}
