/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author dawna.floyd
 */
@Entity
@Table(name = "event_hurricanes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventHurricane.findAll", query = "SELECT e FROM EventHurricane e"),
    @NamedQuery(name = "EventHurricane.findById", query = "SELECT e FROM EventHurricane e WHERE e.eventHurricanePK.id = :id"),
    @NamedQuery(name = "EventHurricane.findByPubdate", query = "SELECT e FROM EventHurricane e WHERE e.eventHurricanePK.pubdate = :pubdate"),
    @NamedQuery(name = "EventHurricane.findByLink", query = "SELECT e FROM EventHurricane e WHERE e.link = :link"),
    @NamedQuery(name = "EventHurricane.findByObjectid", query = "SELECT e FROM EventHurricane e WHERE e.objectid = :objectid"),
    @NamedQuery(name = "EventHurricane.findByName", query = "SELECT e FROM EventHurricane e WHERE e.name = :name"),
    @NamedQuery(name = "EventHurricane.findByType", query = "SELECT e FROM EventHurricane e WHERE e.type = :type"),
    @NamedQuery(name = "EventHurricane.findByMovement", query = "SELECT e FROM EventHurricane e WHERE e.movement = :movement"),
    @NamedQuery(name = "EventHurricane.findByWind", query = "SELECT e FROM EventHurricane e WHERE e.wind = :wind"),
    @NamedQuery(name = "EventHurricane.findByLastModified", query = "SELECT e FROM EventHurricane e WHERE e.lastModified = :lastModified"),
    @NamedQuery(name = "EventHurricane.findByNotificationId", query = "SELECT e FROM EventHurricane e WHERE e.notificationId = :notificationId")})
public class EventHurricane implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @Valid
    protected EventHurricanePK eventHurricanePK;
    
    @Size(max = 80)
    @Column(name = "link")
    private String link;
    
    @Column(name = "geometry")
    private String geometry;
    @JsonProperty("OBJECTID")    
    @Column(name = "objectid")
    private Integer objectid;
    
    @Lob
    @Column(name = "shape")
    private byte[] shape;
    
    @Size(max = 20)
    @Column(name = "name")
    private String name;
    
    @Size(max = 25)
    @Column(name = "type")
    private String type;
    
    @Size(max = 30)
    @Column(name = "movement")
    private String movement;
    
    @Size(max = 10)
    @Column(name = "wind")
    private String wind;
    
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp 
    private Date lastModified;
    
    @Column(name = "notification_id")
    private Integer notificationId;

    public EventHurricane() {
        this.eventHurricanePK = new EventHurricanePK ();
    }

    public EventHurricane(EventHurricanePK eventHurricanePK) {
        this.eventHurricanePK = eventHurricanePK;
    }

    public EventHurricane(String id, Date pubdate) {
        this.eventHurricanePK = new EventHurricanePK(id, pubdate);
    }

        public String getId() {
        return this.eventHurricanePK.getId();
    }

    public void setId(String id) {
        this.eventHurricanePK.setId(id);
    }

    public Date getPubdate() {
        return this.eventHurricanePK.getPubdate();
    }

    public void setPubdate(Date pubdate) {
        this.eventHurricanePK.setPubdate(pubdate);
    }
    
    public EventHurricanePK getEventHurricanePK() {
        return eventHurricanePK;
    }

    public void setEventHurricanePK(EventHurricanePK eventHurricanePK) {
        this.eventHurricanePK = eventHurricanePK;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Integer getObjectid() {
        return objectid;
    }

    public void setObjectid(Integer objectid) {
        this.objectid = objectid;
    }

    public byte[] getShape() {
        return shape;
    }

    public void setShape(byte[] shape) {
        this.shape = shape;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventHurricanePK != null ? eventHurricanePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventHurricane)) {
            return false;
        }
        EventHurricane other = (EventHurricane) object;
        if ((this.eventHurricanePK == null && other.eventHurricanePK != null) || (this.eventHurricanePK != null && !this.eventHurricanePK.equals(other.eventHurricanePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventHurricane[ eventHurricanePK=" + eventHurricanePK + " ]";
    }
    
}
