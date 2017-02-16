/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author dawna.floyd
 */
@Entity
@Table(name = "event_weather")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventWeather.findAll", query = "SELECT e FROM EventWeather e"),
    @NamedQuery(name = "EventWeather.findByWarnid", query = "SELECT e FROM EventWeather e WHERE e.warnid = :warnid"),
    @NamedQuery(name = "EventWeather.findByObjectid", query = "SELECT e FROM EventWeather e WHERE e.objectid = :objectid"),
    @NamedQuery(name = "EventWeather.findByPhenom", query = "SELECT e FROM EventWeather e WHERE e.phenom = :phenom"),
    @NamedQuery(name = "EventWeather.findBySig", query = "SELECT e FROM EventWeather e WHERE e.sig = :sig"),
    @NamedQuery(name = "EventWeather.findByWfo", query = "SELECT e FROM EventWeather e WHERE e.wfo = :wfo"),
    @NamedQuery(name = "EventWeather.findByEvent", query = "SELECT e FROM EventWeather e WHERE e.event = :event"),
    @NamedQuery(name = "EventWeather.findByIssuance", query = "SELECT e FROM EventWeather e WHERE e.issuance = :issuance"),
    @NamedQuery(name = "EventWeather.findByExpiration", query = "SELECT e FROM EventWeather e WHERE e.expiration = :expiration"),
    @NamedQuery(name = "EventWeather.findByUrl", query = "SELECT e FROM EventWeather e WHERE e.url = :url"),
    @NamedQuery(name = "EventWeather.findByMsgType", query = "SELECT e FROM EventWeather e WHERE e.msgType = :msgType"),
    @NamedQuery(name = "EventWeather.findByProdType", query = "SELECT e FROM EventWeather e WHERE e.prodType = :prodType"),
    @NamedQuery(name = "EventWeather.findByIdpSource", query = "SELECT e FROM EventWeather e WHERE e.idpSource = :idpSource"),
    @NamedQuery(name = "EventWeather.findByIdpSubset", query = "SELECT e FROM EventWeather e WHERE e.idpSubset = :idpSubset"),
    @NamedQuery(name = "EventWeather.findByIdpFiledate", query = "SELECT e FROM EventWeather e WHERE e.idpFiledate = :idpFiledate"),
    @NamedQuery(name = "EventWeather.findByIdpIngestdate", query = "SELECT e FROM EventWeather e WHERE e.idpIngestdate = :idpIngestdate"),
    @NamedQuery(name = "EventWeather.findByIdpCurrentForecast", query = "SELECT e FROM EventWeather e WHERE e.idpCurrentForecast = :idpCurrentForecast"),
    @NamedQuery(name = "EventWeather.findByIdpTimeSeries", query = "SELECT e FROM EventWeather e WHERE e.idpTimeSeries = :idpTimeSeries"),
    @NamedQuery(name = "EventWeather.findByIdpIssueddate", query = "SELECT e FROM EventWeather e WHERE e.idpIssueddate = :idpIssueddate"),
    @NamedQuery(name = "EventWeather.findByIdpValidtime", query = "SELECT e FROM EventWeather e WHERE e.idpValidtime = :idpValidtime"),
    @NamedQuery(name = "EventWeather.findByIdpFcstHour", query = "SELECT e FROM EventWeather e WHERE e.idpFcstHour = :idpFcstHour"),
    @NamedQuery(name = "EventWeather.findByStArea", query = "SELECT e FROM EventWeather e WHERE e.stArea = :stArea"),
    @NamedQuery(name = "EventWeather.findByStLength", query = "SELECT e FROM EventWeather e WHERE e.stLength = :stLength"),
    @NamedQuery(name = "EventWeather.findByLastModified", query = "SELECT e FROM EventWeather e WHERE e.lastModified = :lastModified"),
    @NamedQuery(name = "EventWeather.findByNotificationId", query = "SELECT e FROM EventWeather e WHERE e.notificationId = :notificationId")})
public class EventWeather implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "warnid")
    private String warnid;
    
    @Column(name = "objectid")
    private Integer objectid;
    
    @Column(name = "geometry")
    private String geometry;
    
    @Size(max = 2)
    @Column(name = "phenom")
    private String phenom;
    
    @Size(max = 2)
    @Column(name = "sig")
    private String sig;
    
    @Size(max = 4)
    @Column(name = "wfo")
    private String wfo;
    
    @Size(max = 4)
    @Column(name = "event")
    private String event;
    
    @Size(max = 20)
    @Column(name = "issuance")
    private String issuance;
    
    @Size(max = 20)
    @Column(name = "expiration")
    private String expiration;
    
    @Size(max = 128)
    @Column(name = "url")
    private String url;
    
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "msg_type")
    @JsonProperty("msg_type")
    private String msgType;
    
    @NotNull
    @Size(min = 1,max = 64)
    @JsonProperty("prod_type")
    @Column(name = "prod_type")
    private String prodType;
    
    @JsonProperty("idp_source")
    @Size(max = 50)
    @Column(name = "idp_source")
    private String idpSource;
    
    @JsonProperty("idp_subset")
    @Size(max = 50)
    @Column(name = "idp_subset")
    private String idpSubset;
    
    @JsonProperty("idp_filedate")
    @Column(name = "idp_filedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idpFiledate;
    
    @JsonProperty("idp_ingestdate")
    @Column(name = "idp_ingestdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idpIngestdate;
    
    @JsonProperty("idp_current_forecast")
    @Column(name = "idp_current_forecast")
    private Integer idpCurrentForecast;
    
    @JsonProperty("idp_time_series")
    @Column(name = "idp_time_series")
    private Integer idpTimeSeries;
    
    @JsonProperty("idp_issueddate")
    @Column(name = "idp_issueddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idpIssueddate;
    
    @JsonProperty("idp_validtime")
    @Column(name = "idp_validtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idpValidtime;
    
    @JsonProperty("idp_validendtime")
    @Column(name = "idp_validendtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idpValidendtime;
    
    @JsonProperty("idp_fcst_hour")
    @Column(name = "idp_fcst_hour")
    private Integer idpFcstHour;
    
    @Lob
    @Column(name = "shape")
    private byte[] shape;
    @JsonProperty("st_area(shape)")
    @Column(name = "st_area")
    private BigDecimal stArea;
    
    @JsonProperty("st_length(shape)")
    @Column(name = "st_length")
    private BigDecimal stLength;
    
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp 
    private Date lastModified;
    
    @Column(name = "notification_id")
    private Integer notificationId;

    public EventWeather() {
    }

    public EventWeather(String warnid) {
        this.warnid = warnid;
    }

    public String getWarnid() {
        return warnid;
    }

    public void setWarnid(String warnid) {
        this.warnid = warnid;
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

    public String getPhenom() {
        return phenom;
    }

    public void setPhenom(String phenom) {
        this.phenom = phenom;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getWfo() {
        return wfo;
    }

    public void setWfo(String wfo) {
        this.wfo = wfo;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getIssuance() {
        return issuance;
    }

    public void setIssuance(String issuance) {
        this.issuance = issuance;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msg_type) {
        this.msgType = msg_type;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
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

    public Date getIdpFiledate() {
        return idpFiledate;
    }

    public void setIdpFiledate(Date idpFiledate) {
        this.idpFiledate = idpFiledate;
    }

    public Date getIdpIngestdate() {
        return idpIngestdate;
    }

    public void setIdpIngestdate(Date idpIngestdate) {
        this.idpIngestdate = idpIngestdate;
    }

    public Integer getIdpCurrentForecast() {
        return idpCurrentForecast;
    }

    public void setIdpCurrentForecast(Integer idpCurrentForecast) {
        this.idpCurrentForecast = idpCurrentForecast;
    }

    public Integer getIdpTimeSeries() {
        return idpTimeSeries;
    }

    public void setIdpTimeSeries(Integer idpTimeSeries) {
        this.idpTimeSeries = idpTimeSeries;
    }

    public Date getIdpIssueddate() {
        return idpIssueddate;
    }

    public void setIdpIssueddate(Date idpIssueddate) {
        this.idpIssueddate = idpIssueddate;
    }

    public Date getIdpValidtime() {
        return idpValidtime;
    }

    public void setIdpValidtime(Date idpValidtime) {
        this.idpValidtime = idpValidtime;
    }

    public Date getIdpValidendtime() {
        return idpValidendtime;
    }

    public void setIdpValidendtime(Date idpValidendtime) {
        this.idpValidendtime = idpValidendtime;
    }

    public Integer getIdpFcstHour() {
        return idpFcstHour;
    }

    public void setIdpFcstHour(Integer idpFcstHour) {
        this.idpFcstHour = idpFcstHour;
    }

    public byte[] getShape() {
        return shape;
    }

    public void setShape(byte[] shape) {
        this.shape = shape;
    }

    public BigDecimal getStArea() {
        return stArea;
    }

    public void setStArea(BigDecimal stArea) {
        this.stArea = stArea;
    }
    public BigDecimal getStLength() {
        return stLength;
    }

     public void setStLength(BigDecimal stLength) {
        this.stLength = stLength;
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
        hash += (warnid != null ? warnid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventWeather)) {
            return false;
        }
        EventWeather other = (EventWeather) object;
        if ((this.warnid == null && other.warnid != null) || (this.warnid != null && !this.warnid.equals(other.warnid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventWeather[ warnid=" + warnid + " ]";
    } 
    
}
