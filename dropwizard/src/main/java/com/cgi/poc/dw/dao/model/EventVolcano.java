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
@Table(name = "event_volcano")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventVolcano.findAll", query = "SELECT e FROM EventVolcano e"),
    @NamedQuery(name = "EventVolcano.findById", query = "SELECT e FROM EventVolcano e WHERE e.eventVolcanoPK.id = :id"),
    @NamedQuery(name = "EventVolcano.findByPubdate", query = "SELECT e FROM EventVolcano e WHERE e.eventVolcanoPK.pubdate = :pubdate"),
    @NamedQuery(name = "EventVolcano.findByLink", query = "SELECT e FROM EventVolcano e WHERE e.link = :link"),
    @NamedQuery(name = "EventVolcano.findByObjectid", query = "SELECT e FROM EventVolcano e WHERE e.objectid = :objectid"),
    @NamedQuery(name = "EventVolcano.findByAlert", query = "SELECT e FROM EventVolcano e WHERE e.alert = :alert"),
    @NamedQuery(name = "EventVolcano.findByColor", query = "SELECT e FROM EventVolcano e WHERE e.color = :color"),
    @NamedQuery(name = "EventVolcano.findByDescrpt", query = "SELECT e FROM EventVolcano e WHERE e.descrpt = :descrpt"),
    @NamedQuery(name = "EventVolcano.findByLastModified", query = "SELECT e FROM EventVolcano e WHERE e.lastModified = :lastModified"),
    @NamedQuery(name = "EventVolcano.findByNotificationId", query = "SELECT e FROM EventVolcano e WHERE e.notificationId = :notificationId")})
public class EventVolcano implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @Valid
    protected EventVolcanoPK eventVolcanoPK;
    
    @Size(max = 80)
    @Column(name = "link")
    private String link;

    @Column(name = "geometry")
    private String geometry;
    
    @Column(name = "objectid")
    @JsonProperty("OBJECTID")    
    private Integer objectid;
    
    @Lob
    @Column(name = "shape")
    private byte[] shape;
    
    @Size(max = 10)
    @Column(name = "alert")
    private String alert;
    
    @Size(max = 8)
    @Column(name = "color")
    private String color;
    
    @Size(max = 160)
    @Column(name = "descrpt")
    private String descrpt;
    
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp 
    private Date lastModified;
    
    @Column(name = "notification_id")
    private Integer notificationId;

    public EventVolcano() {
                this.eventVolcanoPK = new EventVolcanoPK ();

    }

    public EventVolcano(EventVolcanoPK eventVolcanoPK) {
        this.eventVolcanoPK = eventVolcanoPK;
    }

    public EventVolcano(String id, Date pubdate) {
        this.eventVolcanoPK = new EventVolcanoPK(id, pubdate);
    }

    public EventVolcanoPK getEventVolcanoPK() {
        return eventVolcanoPK;
    }

    public void setEventVolcanoPK(EventVolcanoPK eventVolcanoPK) {
        this.eventVolcanoPK = eventVolcanoPK;
    }
        public String getId() {
        return this.eventVolcanoPK.getId();
    }

    public void setId(String id) {
        this.eventVolcanoPK.setId(id);
    }

    public Date getPubdate() {
        return this.eventVolcanoPK.getPubdate();
    }

    public void setPubdate(Date pubdate) {
        this.eventVolcanoPK.setPubdate(pubdate);
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

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescrpt() {
        return descrpt;
    }

    public void setDescrpt(String descrpt) {
        this.descrpt = descrpt;
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
        hash += (eventVolcanoPK != null ? eventVolcanoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventVolcano)) {
            return false;
        }
        EventVolcano other = (EventVolcano) object;
        if ((this.eventVolcanoPK == null && other.eventVolcanoPK != null) || (this.eventVolcanoPK != null && !this.eventVolcanoPK.equals(other.eventVolcanoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventVolcano[ eventVolcanoPK=" + eventVolcanoPK + " ]";
    }
    
}
