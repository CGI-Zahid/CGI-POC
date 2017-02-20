/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import com.cgi.poc.dw.util.PersistValidationGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author dawna.floyd
 */
@Entity
@Table(name = "event_notification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventNotification.findAll", query = "SELECT e FROM EventNotification e"),
    @NamedQuery(name = "EventNotification.findById", query = "SELECT e FROM EventNotification e WHERE e.id = :id"),
    @NamedQuery(name = "EventNotification.findByDescription", query = "SELECT e FROM EventNotification e WHERE e.description = :description"),
    @NamedQuery(name = "EventNotification.findByType", query = "SELECT e FROM EventNotification e WHERE e.type = :type"),
    @NamedQuery(name = "EventNotification.findByGenerationDate", query = "SELECT e FROM EventNotification e WHERE e.generationDate = :generationDate"),
    @NamedQuery(name = "EventNotification.findByUrl1", query = "SELECT e FROM EventNotification e WHERE e.url1 = :url1"),
    @NamedQuery(name = "EventNotification.findByUrl2", query = "SELECT e FROM EventNotification e WHERE e.url2 = :url2"),
    @NamedQuery(name = "EventNotification.findByCitizensAffected", query = "SELECT e FROM EventNotification e WHERE e.citizensAffected = :citizensAffected"),
    @NamedQuery(name = "EventNotification.findByUserId", query = "SELECT e FROM EventNotification e WHERE e.userId = :userId")})
public class EventNotification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @JsonIgnore
    private Long id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 5, max = 2048)
    @Column(name = "description")
    private String description;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "type")
    private String type;
    
    @Column(name = "generation_date")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp 
    private Date generationDate;
    
    @Size(max = 65535)
    @Column(name = "geometry")
    private String geometry;
    
    @Size(max = 128)
    @Column(name = "url1")
    private String url1;
    
    @Size(max = 128)
    @Column(name = "url2")
    private String url2;
    
    @Column(name = "citizensAffected")
    private Integer citizensAffected;
    
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.PERSIST})
  @NotNull(groups = {PersistValidationGroup.class})
  private User userId;

  @OneToMany(mappedBy = "eventNotificationId", fetch = FetchType.EAGER, orphanRemoval = true)
  @NotNull
  @Valid
  @Cascade({CascadeType.ALL})
  @Fetch(value = FetchMode.SUBSELECT)
  private Set<EventNotificationZipcode> eventNotificationZipcodes;

    public EventNotification() {
    }

    public EventNotification(Long id) {
        this.id = id;
    }

    public EventNotification(Long id, String description, String type) {
        this.id = id;
        this.description = description;
        this.type = type;
     }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public Integer getCitizensAffected() {
        return citizensAffected;
    }

    public void setCitizensAffected(Integer citizensAffected) {
        this.citizensAffected = citizensAffected;
    }

  public User getUserId() {
    return userId;
  }

  public void setUserId(User userId) {
    this.userId = userId;
  }

    public Set<EventNotificationZipcode> getEventNotificationZipcodes() {
    return eventNotificationZipcodes;
  }

  public void setEventNotificationZipcodes(
      Set<EventNotificationZipcode> eventNotificationZipcodes) {
    this.eventNotificationZipcodes = eventNotificationZipcodes;
  }
  
  public void addZipcode(EventNotificationZipcode zipCode) {
     if (zipCode != null) {
        if (this.eventNotificationZipcodes == null) {
            this.eventNotificationZipcodes = new HashSet<EventNotificationZipcode>();          
        }
        this.eventNotificationZipcodes.add(zipCode);
        zipCode.setEventNotificationId(this);
     }
  }
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventNotification)) {
            return false;
        }
        EventNotification other = (EventNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventNotification[ id=" + id + " ]";
    }
    
}
