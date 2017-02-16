/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author dawna.floyd
 */
@Entity
@Table(name = "event_earthquake")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventEarthquake.findAll", query = "SELECT e FROM EventEarthquake e"),
    @NamedQuery(name = "EventEarthquake.findByEqid", query = "SELECT e FROM EventEarthquake e WHERE e.eventEarthquakePK.eqid = :eqid"),
    @NamedQuery(name = "EventEarthquake.findByDatetime", query = "SELECT e FROM EventEarthquake e WHERE e.eventEarthquakePK.datetime = :datetime"),
    @NamedQuery(name = "EventEarthquake.findByObjectid", query = "SELECT e FROM EventEarthquake e WHERE e.objectid = :objectid"),
    @NamedQuery(name = "EventEarthquake.findByDepth", query = "SELECT e FROM EventEarthquake e WHERE e.depth = :depth"),
    @NamedQuery(name = "EventEarthquake.findByLatitude", query = "SELECT e FROM EventEarthquake e WHERE e.latitude = :latitude"),
    @NamedQuery(name = "EventEarthquake.findByLongitude", query = "SELECT e FROM EventEarthquake e WHERE e.longitude = :longitude"),
    @NamedQuery(name = "EventEarthquake.findByMagnitude", query = "SELECT e FROM EventEarthquake e WHERE e.magnitude = :magnitude"),
    @NamedQuery(name = "EventEarthquake.findByNumstations", query = "SELECT e FROM EventEarthquake e WHERE e.numstations = :numstations"),
    @NamedQuery(name = "EventEarthquake.findByRegion", query = "SELECT e FROM EventEarthquake e WHERE e.region = :region"),
    @NamedQuery(name = "EventEarthquake.findBySource", query = "SELECT e FROM EventEarthquake e WHERE e.source = :source"),
    @NamedQuery(name = "EventEarthquake.findByVersion", query = "SELECT e FROM EventEarthquake e WHERE e.version = :version"),
    @NamedQuery(name = "EventEarthquake.findByLastModified", query = "SELECT e FROM EventEarthquake e WHERE e.lastModified = :lastModified"),
    @NamedQuery(name = "EventEarthquake.findByNotificationId", query = "SELECT e FROM EventEarthquake e WHERE e.notificationId = :notificationId")})
public class EventEarthquake implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @Valid
    protected EventEarthquakePK eventEarthquakePK;
    
    @Column(name = "objectid")
    private Integer objectid;
    
    @NotNull
    @Column(name = "geometry")
    private String geometry;
    
    @Column(name = "depth")
    
    private BigDecimal depth;
    @Column(name = "latitude")
    
    private BigDecimal latitude;
    @Column(name = "longitude")
    
    private BigDecimal longitude;
    @Column(name = "magnitude")
    
    private BigDecimal magnitude;
    
    @Column(name = "numstations")
    private Integer numstations;
    
    @Size(max = 200)    
    @Column(name = "region")
    private String region;
    
    @Size(max = 50)    
    @Column(name = "source")
    private String source;
    
    @Size(max = 50)
    @Column(name = "version")
    private String version;
    
    @Lob
    @Column(name = "shape")
    private byte[] shape;
    
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp 
    private Date lastModified;

    @Column(name = "notification_id")
    private Integer notificationId;

    public EventEarthquake() {
        setEventEarthquakePK( new EventEarthquakePK());
    }

    public EventEarthquake(EventEarthquakePK eventEarthquakePK) {
        this.eventEarthquakePK = eventEarthquakePK;
    }

    public EventEarthquake(String eqid, Date datetime) {
        this.eventEarthquakePK = new EventEarthquakePK(eqid, datetime);
    }

    public EventEarthquakePK getEventEarthquakePK() {
        return eventEarthquakePK;
    }

    public void setEventEarthquakePK(EventEarthquakePK eventEarthquakePK) {
        this.eventEarthquakePK = eventEarthquakePK;
    }

    
    public String getEqid() {
        return this.eventEarthquakePK.getEqid();
    }

    public void setEqid(String eqid) {
        this.eventEarthquakePK.setEqid( eqid );
    }

    public Date getDatetime() {
        return this.eventEarthquakePK.getDatetime();
    }

    public void setDatetime(Date datetime) {
        this.eventEarthquakePK.setDatetime(datetime);
    }
    
    public Integer getObjectid() {
        return objectid;
    }

    public void setObjectid(Integer objectid) {
        this.objectid = objectid;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(BigDecimal magnitude) {
        this.magnitude = magnitude;
    }

    public Integer getNumstations() {
        return numstations;
    }

    public void setNumstations(Integer numstations) {
        this.numstations = numstations;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public byte[] getShape() {
        return shape;
    }

    public void setShape(byte[] shape) {
        this.shape = shape;
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
        hash += (eventEarthquakePK != null ? eventEarthquakePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventEarthquake)) {
            return false;
        }
        EventEarthquake other = (EventEarthquake) object;
        if ((this.eventEarthquakePK == null && other.eventEarthquakePK != null) || (this.eventEarthquakePK != null && !this.eventEarthquakePK.equals(other.eventEarthquakePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventEarthquake[ eventEarthquakePK=" + eventEarthquakePK + " ]";
    }
    
}
