/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import com.cgi.poc.dw.util.JsonCoordinate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author dawna.floyd
 */
@Entity
@Table(name = "event_flood")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventFlood.findAll", query = "SELECT e FROM EventFlood e"),
    @NamedQuery(name = "EventFlood.findByWaterbody", query = "SELECT e FROM EventFlood e WHERE e.eventFloodPK.waterbody = :waterbody"),
    @NamedQuery(name = "EventFlood.findByObstime", query = "SELECT e FROM EventFlood e WHERE e.eventFloodPK.obstime = :obstime"),
    @NamedQuery(name = "EventFlood.findByObjectid", query = "SELECT e FROM EventFlood e WHERE e.objectid = :objectid"),
    @NamedQuery(name = "EventFlood.findByLatitude", query = "SELECT e FROM EventFlood e WHERE e.latitude = :latitude"),
    @NamedQuery(name = "EventFlood.findByLongitude", query = "SELECT e FROM EventFlood e WHERE e.longitude = :longitude"),
    @NamedQuery(name = "EventFlood.findByState", query = "SELECT e FROM EventFlood e WHERE e.state = :state"),
    @NamedQuery(name = "EventFlood.findByIdpSource", query = "SELECT e FROM EventFlood e WHERE e.idpSource = :idpSource"),
    @NamedQuery(name = "EventFlood.findByIdpSubset", query = "SELECT e FROM EventFlood e WHERE e.idpSubset = :idpSubset"),
    @NamedQuery(name = "EventFlood.findByGaugelid", query = "SELECT e FROM EventFlood e WHERE e.gaugelid = :gaugelid"),
    @NamedQuery(name = "EventFlood.findByLocation", query = "SELECT e FROM EventFlood e WHERE e.location = :location"),
    @NamedQuery(name = "EventFlood.findByObserved", query = "SELECT e FROM EventFlood e WHERE e.observed = :observed"),
    @NamedQuery(name = "EventFlood.findByUnits", query = "SELECT e FROM EventFlood e WHERE e.units = :units"),
    @NamedQuery(name = "EventFlood.findByAction", query = "SELECT e FROM EventFlood e WHERE e.action = :action"),
    @NamedQuery(name = "EventFlood.findByFlood", query = "SELECT e FROM EventFlood e WHERE e.flood = :flood"),
    @NamedQuery(name = "EventFlood.findByModerate", query = "SELECT e FROM EventFlood e WHERE e.moderate = :moderate"),
    @NamedQuery(name = "EventFlood.findByMajor", query = "SELECT e FROM EventFlood e WHERE e.major = :major"),
    @NamedQuery(name = "EventFlood.findByLowthresh", query = "SELECT e FROM EventFlood e WHERE e.lowthresh = :lowthresh"),
    @NamedQuery(name = "EventFlood.findByLowthreshu", query = "SELECT e FROM EventFlood e WHERE e.lowthreshu = :lowthreshu"),
    @NamedQuery(name = "EventFlood.findByWfo", query = "SELECT e FROM EventFlood e WHERE e.wfo = :wfo"),
    @NamedQuery(name = "EventFlood.findByHdatum", query = "SELECT e FROM EventFlood e WHERE e.hdatum = :hdatum"),
    @NamedQuery(name = "EventFlood.findByPedts", query = "SELECT e FROM EventFlood e WHERE e.pedts = :pedts"),
    @NamedQuery(name = "EventFlood.findBySecvalue", query = "SELECT e FROM EventFlood e WHERE e.secvalue = :secvalue"),
    @NamedQuery(name = "EventFlood.findBySecunit", query = "SELECT e FROM EventFlood e WHERE e.secunit = :secunit"),
    @NamedQuery(name = "EventFlood.findByUrl", query = "SELECT e FROM EventFlood e WHERE e.url = :url"),
    @NamedQuery(name = "EventFlood.findByStatus", query = "SELECT e FROM EventFlood e WHERE e.status = :status"),
    @NamedQuery(name = "EventFlood.findByForecast", query = "SELECT e FROM EventFlood e WHERE e.forecast = :forecast"),
    @NamedQuery(name = "EventFlood.findByLastModified", query = "SELECT e FROM EventFlood e WHERE e.lastModified = :lastModified"),
    @NamedQuery(name = "EventFlood.findByNotificationId", query = "SELECT e FROM EventFlood e WHERE e.notificationId = :notificationId")})
public class EventFlood implements Serializable {

    private static final long serialVersionUID = 1L;
    @Valid
    @EmbeddedId
    protected EventFloodPK eventFloodPK;
    
    @Column(name = "objectid")
    private Integer objectid;
    
    @NotNull
    @Column(name = "geometry")
    private String geometry;
    
// @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitude")
    private BigDecimal latitude;
    
    
    @Column(name = "longitude")
    private BigDecimal longitude;

    @Size(max = 2)
    @Column(name = "state")
    private String state;

    @Size(max = 50)
    @Column(name = "idp_source")
    @JsonProperty("idp_source")
    private String idpSource;

    @Size(max = 50)
    @Column(name = "idp_subset")
    @JsonProperty("idp_subset")
    private String idpSubset;

    @Lob
    @Column(name = "shape")
    private byte[] shape;

    @Size(max = 5)
    @Column(name = "gaugelid")
    private String gaugelid;

    @Size(max = 90)
    @Column(name = "location")
    private String location;

    @Size(max = 24)
    @Column(name = "observed")
    private String observed;

    @Size(max = 5)
    @Column(name = "units")
    private String units;

    @Size(max = 24)
    @Column(name = "action")
    private String action;

    @Size(max = 24)
    @Column(name = "flood")
    private String flood;

    @Size(max = 24)
    @Column(name = "moderate")
    private String moderate;

    @Size(max = 24)
    @Column(name = "major")
    private String major;

    @Size(max = 24)
    @Column(name = "lowthresh")
    private String lowthresh;

    @Size(max = 5)
    @Column(name = "lowthreshu")
    private String lowthreshu;

    @Size(max = 5)
    @Column(name = "wfo")
    private String wfo;

    @Size(max = 30)
    @Column(name = "hdatum")
    private String hdatum;

    @Size(max = 5)
    @Column(name = "pedts")
    private String pedts;

    @Size(max = 24)
    @Column(name = "secvalue")
    private String secvalue;

    @Size(max = 5)
    @Column(name = "secunit")
    private String secunit;

    @Size(max = 128)
    @Column(name = "url")
    private String url;

    @Size(max = 25)
    @Column(name = "status")
    private String status;

    @Size(max = 24)
    @Column(name = "forecast")
    private String forecast;

    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp 
    private Date lastModified;

    @Column(name = "notification_id")
    private Integer notificationId;

    public EventFlood() {
       this.eventFloodPK =  new EventFloodPK();
    }

    public EventFlood(EventFloodPK eventFloodPK) {
        this.eventFloodPK = eventFloodPK;
    }

    public EventFlood(String waterbody, String obstime) {
        this.eventFloodPK = new EventFloodPK(waterbody, obstime);
    }

    public EventFloodPK getEventFloodPK() {
        return eventFloodPK;
    }

    public void setEventFloodPK(EventFloodPK eventFloodPK) {
        this.eventFloodPK = eventFloodPK;
    }

        public String getWaterbody() {
        return this.eventFloodPK.getWaterbody();
    }

    public void setWaterbody(String waterbody) {
          this.eventFloodPK.setWaterbody (waterbody);
    }

    public String getObstime() {
        return this.eventFloodPK.getObstime();
    }

    public void setObstime(String obstime) {
          this.eventFloodPK.setObstime(obstime);
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

    public BigDecimal getLatitude() {
        return latitude;
    }

    @JsonDeserialize(using = JsonCoordinate.class)
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    @JsonDeserialize(using = JsonCoordinate.class)
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdpSource() {
        return idpSource;
    }

    public void setIdpSource(String idpSource) {
        this.idpSource = idpSource;
    }

    public String getIdpSubset() {
        return idpSubset;
    }

    public void setIdpSubset(String idpSubset) {
        this.idpSubset = idpSubset;
    }

    public byte[] getShape() {
        return shape;
    }

    public void setShape(byte[] shape) {
        this.shape = shape;
    }

    public String getGaugelid() {
        return gaugelid;
    }

    public void setGaugelid(String gaugelid) {
        this.gaugelid = gaugelid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getObserved() {
        return observed;
    }

    public void setObserved(String observed) {
        this.observed = observed;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFlood() {
        return flood;
    }

    public void setFlood(String flood) {
        this.flood = flood;
    }

    public String getModerate() {
        return moderate;
    }

    public void setModerate(String moderate) {
        this.moderate = moderate;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getLowthresh() {
        return lowthresh;
    }

    public void setLowthresh(String lowthresh) {
        this.lowthresh = lowthresh;
    }

    public String getLowthreshu() {
        return lowthreshu;
    }

    public void setLowthreshu(String lowthreshu) {
        this.lowthreshu = lowthreshu;
    }

    public String getWfo() {
        return wfo;
    }

    public void setWfo(String wfo) {
        this.wfo = wfo;
    }

    public String getHdatum() {
        return hdatum;
    }

    public void setHdatum(String hdatum) {
        this.hdatum = hdatum;
    }

    public String getPedts() {
        return pedts;
    }

    public void setPedts(String pedts) {
        this.pedts = pedts;
    }

    public String getSecvalue() {
        return secvalue;
    }

    public void setSecvalue(String secvalue) {
        this.secvalue = secvalue;
    }

    public String getSecunit() {
        return secunit;
    }

    public void setSecunit(String secunit) {
        this.secunit = secunit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
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
        hash += (eventFloodPK != null ? eventFloodPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventFlood)) {
            return false;
        }
        EventFlood other = (EventFlood) object;
        if ((this.eventFloodPK == null && other.eventFloodPK != null) || (this.eventFloodPK != null && !this.eventFloodPK.equals(other.eventFloodPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventFlood[ eventFloodPK=" + eventFloodPK + " ]";
    }
    
}
