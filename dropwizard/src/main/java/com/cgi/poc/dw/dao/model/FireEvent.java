/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "event_fire")
@XmlRootElement

public class FireEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "uniquefireidentifier")
    private String uniquefireidentifier;
    
    @NotNull
    @Column(name = "geometry")
    private String geometry;
    
    @Size(max = 20)
    @Column(name = "incidentname")
    private String incidentname;
    
    @Size(max = 60)
    @Column(name = "hotlink")
    private String hotlink;
    
    @Size(max = 1)
    @Column(name = "status")
    private String status;
    
    @Column(name = "objectid")
    private Integer objectid;
    
// @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitude")
    private BigDecimal latitude;
    
    @Column(name = "longitude")
    private BigDecimal longitude;
    
    @Column(name = "acres")
    private BigDecimal acres;
    
    @Size(max = 4)
    @Column(name = "gacc")
    private String gacc;
    
    @Size(max = 2)
    @Column(name = "state")
    private String state;
    
    @Lob
    @Column(name = "shape")
    private byte[] shape;
    
    @Size(max = 38)
    @Column(name = "irwinid")
    private String irwinid;
    
    @Size(max = 5)
    @Column(name = "iscomplex")
    private String iscomplex;
    
    @Size(max = 38)
    @Column(name = "complexparentirwinid")
    private String complexparentirwinid;
    
    @Size(max = 15)
    @Column(name = "firecause")
    private String firecause;
    
    @Column(name = "reportdatetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportdatetime;
    
    @Column(name = "percentcontained")
    private Integer percentcontained;
    
    @Column(name = "firediscoverydatetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firediscoverydatetime;
    
    @Size(max = 6)
    @Column(name = "pooresponsibleunit")
    private String pooresponsibleunit;
    
    @Column(name = "irwinmodifiedon")
    @Temporal(TemporalType.TIMESTAMP)
    private Date irwinmodifiedon;
    
    @Size(max = 1)
    @Column(name = "mapsymbol")
    private String mapsymbol;
    
    @Column(name = "datecurrent")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecurrent;
    
    @Size(max = 6)
    @Column(name = "pooownerunit")
    private String pooownerunit;
    
    @Size(max = 15)
    @Column(name = "owneragency")
    private String owneragency;
    
    @Column(name = "fireyear")
    private Integer fireyear;
    
    @Size(max = 10)
    @Column(name = "localincidentidentifier")
    private String localincidentidentifier;
    
    @Size(max = 2)
    @Column(name = "incidenttypecategory")
    private String incidenttypecategory;
    
    @Basic(optional = false)
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp 
    private Date lastModified;
    
    @Column(name = "notification_id")
    private Integer notificationId;

    public FireEvent() {
    }

    public FireEvent(String uniquefireidentifier) {
        this.uniquefireidentifier = uniquefireidentifier;
    }

    public String getUniquefireidentifier() {
        return uniquefireidentifier;
    }

    public void setUniquefireidentifier(String uniquefireidentifier) {
        this.uniquefireidentifier = uniquefireidentifier;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getIncidentname() {
        return incidentname;
    }

    public void setIncidentname(String incidentname) {
        this.incidentname = incidentname;
    }

    public String getHotlink() {
        return hotlink;
    }

    public void setHotlink(String hotlink) {
        this.hotlink = hotlink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getObjectid() {
        return objectid;
    }

    public void setObjectid(Integer objectid) {
        this.objectid = objectid;
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

    public BigDecimal getAcres() {
        return acres;
    }

    public void setAcres(BigDecimal acres) {
        this.acres = acres;
    }

    public String getGacc() {
        return gacc;
    }

    public void setGacc(String gacc) {
        this.gacc = gacc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public byte[] getShape() {
        return shape;
    }

    public void setShape(byte[] shape) {
        this.shape = shape;
    }

    public String getIrwinid() {
        return irwinid;
    }

    public void setIrwinid(String irwinid) {
        this.irwinid = irwinid;
    }

    public String getIscomplex() {
        return iscomplex;
    }

    public void setIscomplex(String iscomplex) {
        this.iscomplex = iscomplex;
    }

    public String getComplexparentirwinid() {
        return complexparentirwinid;
    }

    public void setComplexparentirwinid(String complexparentirwinid) {
        this.complexparentirwinid = complexparentirwinid;
    }

    public String getFirecause() {
        return firecause;
    }

    public void setFirecause(String firecause) {
        this.firecause = firecause;
    }

    public Date getReportdatetime() {
        return reportdatetime;
    }

    public void setReportdatetime(Date reportdatetime) {
        this.reportdatetime = reportdatetime;
    }

    public Integer getPercentcontained() {
        return percentcontained;
    }

    public void setPercentcontained(Integer percentcontained) {
        this.percentcontained = percentcontained;
    }

    public Date getFirediscoverydatetime() {
        return firediscoverydatetime;
    }

    public void setFirediscoverydatetime(Date firediscoverydatetime) {
        this.firediscoverydatetime = firediscoverydatetime;
    }

    public String getPooresponsibleunit() {
        return pooresponsibleunit;
    }

    public void setPooresponsibleunit(String pooresponsibleunit) {
        this.pooresponsibleunit = pooresponsibleunit;
    }

    public Date getIrwinmodifiedon() {
        return irwinmodifiedon;
    }

    public void setIrwinmodifiedon(Date irwinmodifiedon) {
        this.irwinmodifiedon = irwinmodifiedon;
    }

    public String getMapsymbol() {
        return mapsymbol;
    }

    public void setMapsymbol(String mapsymbol) {
        this.mapsymbol = mapsymbol;
    }

    public Date getDatecurrent() {
        return datecurrent;
    }

    public void setDatecurrent(Date datecurrent) {
        this.datecurrent = datecurrent;
    }

    public String getPooownerunit() {
        return pooownerunit;
    }

    public void setPooownerunit(String pooownerunit) {
        this.pooownerunit = pooownerunit;
    }

    public String getOwneragency() {
        return owneragency;
    }

    public void setOwneragency(String owneragency) {
        this.owneragency = owneragency;
    }

    public Integer getFireyear() {
        return fireyear;
    }

    public void setFireyear(Integer fireyear) {
        this.fireyear = fireyear;
    }

    public String getLocalincidentidentifier() {
        return localincidentidentifier;
    }

    public void setLocalincidentidentifier(String localincidentidentifier) {
        this.localincidentidentifier = localincidentidentifier;
    }

    public String getIncidenttypecategory() {
        return incidenttypecategory;
    }

    public void setIncidenttypecategory(String incidenttypecategory) {
        this.incidenttypecategory = incidenttypecategory;
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
        hash += (uniquefireidentifier != null ? uniquefireidentifier.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FireEvent)) {
            return false;
        }
        FireEvent other = (FireEvent) object;
        if ((this.uniquefireidentifier == null && other.uniquefireidentifier != null) || (this.uniquefireidentifier != null && !this.uniquefireidentifier.equals(other.uniquefireidentifier))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventFire[ uniquefireidentifier=" + uniquefireidentifier + " ]";
    }
    
}