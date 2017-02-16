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
@Table(name = "event_tsunami")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventTsunami.findAll", query = "SELECT e FROM EventTsunami e"),
    @NamedQuery(name = "EventTsunami.findById", query = "SELECT e FROM EventTsunami e WHERE e.id = :id"),
    @NamedQuery(name = "EventTsunami.findByYear", query = "SELECT e FROM EventTsunami e WHERE e.year = :year"),
    @NamedQuery(name = "EventTsunami.findByMonth", query = "SELECT e FROM EventTsunami e WHERE e.month = :month"),
    @NamedQuery(name = "EventTsunami.findByDay", query = "SELECT e FROM EventTsunami e WHERE e.day = :day"),
    @NamedQuery(name = "EventTsunami.findByHour", query = "SELECT e FROM EventTsunami e WHERE e.hour = :hour"),
    @NamedQuery(name = "EventTsunami.findByMinute", query = "SELECT e FROM EventTsunami e WHERE e.minute = :minute"),
    @NamedQuery(name = "EventTsunami.findBySecond", query = "SELECT e FROM EventTsunami e WHERE e.second = :second"),
    @NamedQuery(name = "EventTsunami.findByDateString", query = "SELECT e FROM EventTsunami e WHERE e.dateString = :dateString"),
    @NamedQuery(name = "EventTsunami.findByLatitude", query = "SELECT e FROM EventTsunami e WHERE e.latitude = :latitude"),
    @NamedQuery(name = "EventTsunami.findByLongitude", query = "SELECT e FROM EventTsunami e WHERE e.longitude = :longitude"),
    @NamedQuery(name = "EventTsunami.findByLocationName", query = "SELECT e FROM EventTsunami e WHERE e.locationName = :locationName"),
    @NamedQuery(name = "EventTsunami.findByArea", query = "SELECT e FROM EventTsunami e WHERE e.area = :area"),
    @NamedQuery(name = "EventTsunami.findByCountry", query = "SELECT e FROM EventTsunami e WHERE e.country = :country"),
    @NamedQuery(name = "EventTsunami.findByRegionCode", query = "SELECT e FROM EventTsunami e WHERE e.regionCode = :regionCode"),
    @NamedQuery(name = "EventTsunami.findByRegion", query = "SELECT e FROM EventTsunami e WHERE e.region = :region"),
    @NamedQuery(name = "EventTsunami.findByCauseCode", query = "SELECT e FROM EventTsunami e WHERE e.causeCode = :causeCode"),
    @NamedQuery(name = "EventTsunami.findByCause", query = "SELECT e FROM EventTsunami e WHERE e.cause = :cause"),
    @NamedQuery(name = "EventTsunami.findByEventValidityCode", query = "SELECT e FROM EventTsunami e WHERE e.eventValidityCode = :eventValidityCode"),
    @NamedQuery(name = "EventTsunami.findByEventValidity", query = "SELECT e FROM EventTsunami e WHERE e.eventValidity = :eventValidity"),
    @NamedQuery(name = "EventTsunami.findByEqMagUnk", query = "SELECT e FROM EventTsunami e WHERE e.eqMagUnk = :eqMagUnk"),
    @NamedQuery(name = "EventTsunami.findByEqMagMb", query = "SELECT e FROM EventTsunami e WHERE e.eqMagMb = :eqMagMb"),
    @NamedQuery(name = "EventTsunami.findByEqMagMs", query = "SELECT e FROM EventTsunami e WHERE e.eqMagMs = :eqMagMs"),
    @NamedQuery(name = "EventTsunami.findByEqMagMw", query = "SELECT e FROM EventTsunami e WHERE e.eqMagMw = :eqMagMw"),
    @NamedQuery(name = "EventTsunami.findByEqMagMl", query = "SELECT e FROM EventTsunami e WHERE e.eqMagMl = :eqMagMl"),
    @NamedQuery(name = "EventTsunami.findByEqMagMta", query = "SELECT e FROM EventTsunami e WHERE e.eqMagMta = :eqMagMta"),
    @NamedQuery(name = "EventTsunami.findByEqMagnatude", query = "SELECT e FROM EventTsunami e WHERE e.eqMagnatude = :eqMagnatude"),
    @NamedQuery(name = "EventTsunami.findByEqMagnatudeRank", query = "SELECT e FROM EventTsunami e WHERE e.eqMagnatudeRank = :eqMagnatudeRank"),
    @NamedQuery(name = "EventTsunami.findByEqDepth", query = "SELECT e FROM EventTsunami e WHERE e.eqDepth = :eqDepth"),
    @NamedQuery(name = "EventTsunami.findByMaxEventRunup", query = "SELECT e FROM EventTsunami e WHERE e.maxEventRunup = :maxEventRunup"),
    @NamedQuery(name = "EventTsunami.findByTsMtAbe", query = "SELECT e FROM EventTsunami e WHERE e.tsMtAbe = :tsMtAbe"),
    @NamedQuery(name = "EventTsunami.findByTsMtIi", query = "SELECT e FROM EventTsunami e WHERE e.tsMtIi = :tsMtIi"),
    @NamedQuery(name = "EventTsunami.findByTsIntensity", query = "SELECT e FROM EventTsunami e WHERE e.tsIntensity = :tsIntensity"),
    @NamedQuery(name = "EventTsunami.findByDamageMillionsDollars", query = "SELECT e FROM EventTsunami e WHERE e.damageMillionsDollars = :damageMillionsDollars"),
    @NamedQuery(name = "EventTsunami.findByDamageAmountOrder", query = "SELECT e FROM EventTsunami e WHERE e.damageAmountOrder = :damageAmountOrder"),
    @NamedQuery(name = "EventTsunami.findByDamageDescription", query = "SELECT e FROM EventTsunami e WHERE e.damageDescription = :damageDescription"),
    @NamedQuery(name = "EventTsunami.findByHousesDestroyed", query = "SELECT e FROM EventTsunami e WHERE e.housesDestroyed = :housesDestroyed"),
    @NamedQuery(name = "EventTsunami.findByHousesAmountOrder", query = "SELECT e FROM EventTsunami e WHERE e.housesAmountOrder = :housesAmountOrder"),
    @NamedQuery(name = "EventTsunami.findByHousesDescription", query = "SELECT e FROM EventTsunami e WHERE e.housesDescription = :housesDescription"),
    @NamedQuery(name = "EventTsunami.findByDeaths", query = "SELECT e FROM EventTsunami e WHERE e.deaths = :deaths"),
    @NamedQuery(name = "EventTsunami.findByDeathsAmountOrder", query = "SELECT e FROM EventTsunami e WHERE e.deathsAmountOrder = :deathsAmountOrder"),
    @NamedQuery(name = "EventTsunami.findByDeathsDescription", query = "SELECT e FROM EventTsunami e WHERE e.deathsDescription = :deathsDescription"),
    @NamedQuery(name = "EventTsunami.findByInjuries", query = "SELECT e FROM EventTsunami e WHERE e.injuries = :injuries"),
    @NamedQuery(name = "EventTsunami.findByInjuriesAmountOrder", query = "SELECT e FROM EventTsunami e WHERE e.injuriesAmountOrder = :injuriesAmountOrder"),
    @NamedQuery(name = "EventTsunami.findByInjuriesDescription", query = "SELECT e FROM EventTsunami e WHERE e.injuriesDescription = :injuriesDescription"),
    @NamedQuery(name = "EventTsunami.findByMissing", query = "SELECT e FROM EventTsunami e WHERE e.missing = :missing"),
    @NamedQuery(name = "EventTsunami.findByMissingAmountOrder", query = "SELECT e FROM EventTsunami e WHERE e.missingAmountOrder = :missingAmountOrder"),
    @NamedQuery(name = "EventTsunami.findByMissingDescription", query = "SELECT e FROM EventTsunami e WHERE e.missingDescription = :missingDescription"),
    @NamedQuery(name = "EventTsunami.findByComments", query = "SELECT e FROM EventTsunami e WHERE e.comments = :comments"),
    @NamedQuery(name = "EventTsunami.findByNumRunup", query = "SELECT e FROM EventTsunami e WHERE e.numRunup = :numRunup"),
    @NamedQuery(name = "EventTsunami.findByDamageMillionsDollarsTotal", query = "SELECT e FROM EventTsunami e WHERE e.damageMillionsDollarsTotal = :damageMillionsDollarsTotal"),
    @NamedQuery(name = "EventTsunami.findByDamageAmountOrderTotal", query = "SELECT e FROM EventTsunami e WHERE e.damageAmountOrderTotal = :damageAmountOrderTotal"),
    @NamedQuery(name = "EventTsunami.findByDamageTotalDescription", query = "SELECT e FROM EventTsunami e WHERE e.damageTotalDescription = :damageTotalDescription"),
    @NamedQuery(name = "EventTsunami.findByHousesDestroyedTotal", query = "SELECT e FROM EventTsunami e WHERE e.housesDestroyedTotal = :housesDestroyedTotal"),
    @NamedQuery(name = "EventTsunami.findByHousesAmountOrderTotal", query = "SELECT e FROM EventTsunami e WHERE e.housesAmountOrderTotal = :housesAmountOrderTotal"),
    @NamedQuery(name = "EventTsunami.findByHousesTotalDescription", query = "SELECT e FROM EventTsunami e WHERE e.housesTotalDescription = :housesTotalDescription"),
    @NamedQuery(name = "EventTsunami.findByDeathsTotal", query = "SELECT e FROM EventTsunami e WHERE e.deathsTotal = :deathsTotal"),
    @NamedQuery(name = "EventTsunami.findByDeathsAmountOrderTotal", query = "SELECT e FROM EventTsunami e WHERE e.deathsAmountOrderTotal = :deathsAmountOrderTotal"),
    @NamedQuery(name = "EventTsunami.findByDeathsTotalDescription", query = "SELECT e FROM EventTsunami e WHERE e.deathsTotalDescription = :deathsTotalDescription"),
    @NamedQuery(name = "EventTsunami.findByInjuriesTotal", query = "SELECT e FROM EventTsunami e WHERE e.injuriesTotal = :injuriesTotal"),
    @NamedQuery(name = "EventTsunami.findByInjuriesAmountOrderTotal", query = "SELECT e FROM EventTsunami e WHERE e.injuriesAmountOrderTotal = :injuriesAmountOrderTotal"),
    @NamedQuery(name = "EventTsunami.findByInjuriesTotalDescription", query = "SELECT e FROM EventTsunami e WHERE e.injuriesTotalDescription = :injuriesTotalDescription"),
    @NamedQuery(name = "EventTsunami.findByMissingTotal", query = "SELECT e FROM EventTsunami e WHERE e.missingTotal = :missingTotal"),
    @NamedQuery(name = "EventTsunami.findByMissingAmountOrderTotal", query = "SELECT e FROM EventTsunami e WHERE e.missingAmountOrderTotal = :missingAmountOrderTotal"),
    @NamedQuery(name = "EventTsunami.findByMissingTotalDescription", query = "SELECT e FROM EventTsunami e WHERE e.missingTotalDescription = :missingTotalDescription"),
    @NamedQuery(name = "EventTsunami.findByObjectid", query = "SELECT e FROM EventTsunami e WHERE e.objectid = :objectid"),
    @NamedQuery(name = "EventTsunami.findByHousesDamaged", query = "SELECT e FROM EventTsunami e WHERE e.housesDamaged = :housesDamaged"),
    @NamedQuery(name = "EventTsunami.findByHousesDamagedAmountOrder", query = "SELECT e FROM EventTsunami e WHERE e.housesDamagedAmountOrder = :housesDamagedAmountOrder"),
    @NamedQuery(name = "EventTsunami.findByHousesDamDescription", query = "SELECT e FROM EventTsunami e WHERE e.housesDamDescription = :housesDamDescription"),
    @NamedQuery(name = "EventTsunami.findByHousesDamagedTotal", query = "SELECT e FROM EventTsunami e WHERE e.housesDamagedTotal = :housesDamagedTotal"),
    @NamedQuery(name = "EventTsunami.findByHousesDamaAmountOrderTotal", query = "SELECT e FROM EventTsunami e WHERE e.housesDamaAmountOrderTotal = :housesDamaAmountOrderTotal"),
    @NamedQuery(name = "EventTsunami.findByHousesDamTotalDescription", query = "SELECT e FROM EventTsunami e WHERE e.housesDamTotalDescription = :housesDamTotalDescription"),
    @NamedQuery(name = "EventTsunami.findByNumDeposits", query = "SELECT e FROM EventTsunami e WHERE e.numDeposits = :numDeposits"),
    @NamedQuery(name = "EventTsunami.findByUrl", query = "SELECT e FROM EventTsunami e WHERE e.url = :url"),
    @NamedQuery(name = "EventTsunami.findByLastModified", query = "SELECT e FROM EventTsunami e WHERE e.lastModified = :lastModified"),
    @NamedQuery(name = "EventTsunami.findByNotificationId", query = "SELECT e FROM EventTsunami e WHERE e.notificationId = :notificationId")})
public class EventTsunami implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @JsonProperty("ID")
    private BigDecimal id;
    
    @Column(name = "geometry")
    private String geometry;
    
    @Column(name = "year")
    @JsonProperty("YEAR")
    private Integer year;
    
    @Column(name = "month")
    @JsonProperty("MONTH")
    private Integer month;
    
    @Column(name = "day")
    @JsonProperty("DAY")
    private Integer day;
    
    @JsonProperty("HOUR")
    @Column(name = "hour")
    private Integer hour;
    
    @JsonProperty("MINUTE")
    @Column(name = "minute")
    private Integer minute;
    
    @JsonProperty("SECOND")
    @Column(name = "second")
    private BigDecimal second;
    
    @JsonProperty("DATE_STRING")
    @Size(max = 13)
    @Column(name = "date_string")
    private String dateString;
    
    @JsonProperty("LATITUDE")
    @Column(name = "latitude")
    private BigDecimal latitude;
    
    @JsonProperty("LONGITUDE")
    @Column(name = "longitude")
    private BigDecimal longitude;
    
    @JsonProperty("LOCATION_NAME")
    @Size(max = 13)
    @Column(name = "location_name")
    private String locationName;
    
    @JsonProperty("AREA")
    @Size(max = 2)
    @Column(name = "area")
    private String area;
    
    @JsonProperty("COUNTRY")
    @Size(max = 50)
    @Column(name = "country")
    private String country;
    
    @JsonProperty("REGION_CODE")
    @Column(name = "region_code")
    private Integer regionCode;
    
    @JsonProperty("REGION")
    @Size(max = 50)
    @Column(name = "region")
    private String region;
    
    @JsonProperty("CAUSE_CODE")
    @Column(name = "cause_code")
    private Integer causeCode;
    
    @JsonProperty("CAUSE")
    @Size(max = 50)
    @Column(name = "cause")
    private String cause;
    
    @JsonProperty("EVENT_VALIDITY_CODE")
    @Column(name = "event_validity_code")
    private Integer eventValidityCode;
    
    @JsonProperty("EVENT_VALIDITY")
    @Size(max = 100)
    @Column(name = "event_validity")
    private String eventValidity;
    
    @JsonProperty("EQ_MAG_UNK")
    @Column(name = "eq_mag_unk")
    private BigDecimal eqMagUnk;
    
    @Column(name = "eq_mag_mb")
    @JsonProperty("EQ_MAG_MB")
    private BigDecimal eqMagMb;
    
    @Column(name = "eq_mag_ms")
    @JsonProperty("EQ_MAG_MS")
    private BigDecimal eqMagMs;
    
    @Column(name = "eq_mag_mw")
    @JsonProperty("EQ_MAG_MW")
    private BigDecimal eqMagMw;
    
    @Column(name = "eq_mag_ml")
    @JsonProperty("EQ_MAG_ML")
    private BigDecimal eqMagMl;
    
    @Column(name = "eq_mag_mta")
    @JsonProperty("EQ_MAG_MFA")
    private BigDecimal eqMagMta;
    
    @Column(name = "eq_magnatude")
    @JsonProperty("EQ_MAGNITUDE")
    private BigDecimal eqMagnatude;
    
    @Column(name = "eq_magnatude_rank")
    @JsonProperty("EQ_MAGNITUDE_RANK")
    private BigDecimal eqMagnatudeRank;
    
    @Column(name = "eq_depth")
    @JsonProperty("EQ_DEPTH")
    private Integer eqDepth;
    
    @Column(name = "max_event_runup")
    @JsonProperty("MAX_EVENT_RUNUP")
    private BigDecimal maxEventRunup;
    
    @Column(name = "TS_MT_ABE")
    @JsonProperty("TS_MT_ABE")
    private BigDecimal tsMtAbe;
    
    @Column(name = "ts_mt_ii")
    @JsonProperty("TS_MT_II")
    private BigDecimal tsMtIi;
    
    @Column(name = "ts_intensity")
    @JsonProperty("TS_INTENSITY")
    private BigDecimal tsIntensity;
    
    @Column(name = "damage_millions_dollars")
    @JsonProperty("DAMAGE_MILLIONS_DOLLARS")
    private BigDecimal damageMillionsDollars;
    
    @Column(name = "damage_amount_order")
    @JsonProperty("DAMAGE_AMOUNT_ORDER")
    private Integer damageAmountOrder;
    
    @JsonProperty("DAMAGE_DESCRIPTION")
    @Size(max = 40)
    @Column(name = "damage_description")
    private String damageDescription;
    
    @Column(name = "houses_destroyed")
    @JsonProperty("HOUSES_DESTROYED")
    private Integer housesDestroyed;
    
    @Column(name = "houses_amount_order")
    @JsonProperty("HOUSES_AMOUNT_ORDER")
    private Integer housesAmountOrder;
    
    @Size(max = 40)
    @Column(name = "houses_description")
    @JsonProperty("HOUSES_DESCRIPTION")
    private String housesDescription;
    
    @Column(name = "deaths")
    @JsonProperty("DEATHS")
    private Integer deaths;
    
    @Column(name = "deaths_amount_order")
    @JsonProperty("DEATHS_AMOUNT_ORDER")
    private Integer deathsAmountOrder;
    
    @Size(max = 40)
    @JsonProperty("DEATHS_DESCRIPTION")
    @Column(name = "deaths_description")
    private String deathsDescription;
    
    @Column(name = "injuries")
    @JsonProperty("INJURIES")
    private Integer injuries;
    
    @Column(name = "injuries_amount_order")
    @JsonProperty("INJURIES_AMOUNT_ORDER")
    private Integer injuriesAmountOrder;
    
    @JsonProperty("INJURIES_DESCRIPTION")
    @Size(max = 40)
    @Column(name = "injuries_description")
    private String injuriesDescription;
    
    @JsonProperty("MISSING")
    @Column(name = "missing")
    private Integer missing;
    
    @JsonProperty("MISSING_AMOUNT_ORDER")
    @Column(name = "missing_amount_order")
    private Integer missingAmountOrder;
    
    @JsonProperty("MISSING_DESCRIPTION")
    @Size(max = 40)
    @Column(name = "missing_description")
    private String missingDescription;
    
    @JsonProperty("COMMENTS")
    @Size(max = 4000)
    @Column(name = "comments")
    private String comments;
    
    @JsonProperty("NUM_RUNUP")
    @Column(name = "num_runup")
    private Integer numRunup;
    
    @Column(name = "damage_millions_dollars_total")
    @JsonProperty("DAMAGE_MILLIONS_DOLLARS_TOTAL")
    private BigDecimal damageMillionsDollarsTotal;
    
    @Column(name = "damage_amount_order_total")
    @JsonProperty("DAMAGE_AMOUNT_ORDER_TOTAL")
    private Integer damageAmountOrderTotal;
    
    @Size(max = 40)
    @JsonProperty("DAMAGE_TOTAL_DESCRIPTION")
    @Column(name = "damage_total_description")
    private String damageTotalDescription;
    
    @JsonProperty("HOUSES_DESTROYED_TOTAL")
    @Column(name = "houses_destroyed_total")
    private Integer housesDestroyedTotal;
    
    @Column(name = "houses_amount_order_total")
    @JsonProperty("HOUSES_AMOUNT_ORDER_TOTAL")
    private Integer housesAmountOrderTotal;
    
    @Size(max = 40)
    @JsonProperty("HOUSES_TOTAL_DESCRIPTION")
    @Column(name = "houses_total_description")
    private String housesTotalDescription;
    
    @JsonProperty("DEATHS_TOTAL")
    @Column(name = "deaths_total")
    private Integer deathsTotal;
    
    @Column(name = "deaths_amount_order_total")
    @JsonProperty("DEATHS_AMOUNT_ORDER_TOTAL")
    private Integer deathsAmountOrderTotal;
    
    @Size(max = 40)
    @JsonProperty("DEATHS_TOTAL_DESCRIPTION")
    @Column(name = "deaths_total_description")
    private String deathsTotalDescription;
    
    @Column(name = "injuries_total")
    @JsonProperty("INJURIES_TOTAL")
    private Integer injuriesTotal;
    
    @Column(name = "injuries_amount_order_total")
    @JsonProperty("INJURIES_AMOUNT_ORDER_TOTAL")
    private Integer injuriesAmountOrderTotal;
    
    @Size(max = 40)
    @JsonProperty("INJURIES_TOTAL_DESCRIPTION")
    @Column(name = "injuries_total_description")
    private String injuriesTotalDescription;
    
    @JsonProperty("MISSING_TOTAL")
    @Column(name = "missing_total")
    private Integer missingTotal;
    
    @JsonProperty("MISSING_AMOUNT_ORDER_TOTAL")
    @Column(name = "missing_amount_order_total")
    private Integer missingAmountOrderTotal;
    
    @JsonProperty("MISSING_TOTAL_DESCRIPTION")
    @Size(max = 40)
    @Column(name = "missing_total_description")
    private String missingTotalDescription;
    
    @JsonProperty("OBJECTID")
    @Column(name = "objectid")
    private Integer objectid;
    
    @Lob
    @Column(name = "shape")
    private byte[] shape;
    
    @JsonProperty("HOUSES_DAMAGED")
    @Column(name = "houses_damaged")
    private Integer housesDamaged;
    
    @JsonProperty("HOUSES_DAMAGED_AMOUNT_ORDER")
    @Column(name = "houses_damaged_amount_order")
    private Integer housesDamagedAmountOrder;
    
    @JsonProperty("HOUSES_DAM_DESCRIPTION")
    @Size(max = 40)
    @Column(name = "houses_dam_description")
    private String housesDamDescription;
    
    @JsonProperty("HOUSES_DAMAGED_TOTAL")
    @Column(name = "houses_damaged_total")
    private Integer housesDamagedTotal;
    
    @Column(name = "houses_dama_amount_order_total")
    @JsonProperty("HOUSES_DAM_AMOUNT_ORDER_TOTAL")
    private Integer housesDamaAmountOrderTotal;
    
    @Size(max = 40)
    @JsonProperty("HOUSES_DAM_TOTAL_DESCRIPTION")
    @Column(name = "houses_dam_total_description")
    private String housesDamTotalDescription;
    
    @JsonProperty("NUM_DEPOSITS")
    @Column(name = "num_deposits")
    private Integer numDeposits;
    
    @JsonProperty("URL")
    @Size(max = 128)
    @Column(name = "url")
    private String url;
    
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp 
    private Date lastModified;
    
    @Column(name = "notification_id")
    private Integer notificationId;

    public EventTsunami() {
    }

    public EventTsunami(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public BigDecimal getSecond() {
        return second;
    }

    public void setSecond(BigDecimal second) {
        this.second = second;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getCauseCode() {
        return causeCode;
    }

    public void setCauseCode(Integer causeCode) {
        this.causeCode = causeCode;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Integer getEventValidityCode() {
        return eventValidityCode;
    }

    public void setEventValidityCode(Integer eventValidityCode) {
        this.eventValidityCode = eventValidityCode;
    }

    public String getEventValidity() {
        return eventValidity;
    }

    public void setEventValidity(String eventValidity) {
        this.eventValidity = eventValidity;
    }

    public BigDecimal getEqMagUnk() {
        return eqMagUnk;
    }

    public void setEqMagUnk(BigDecimal eqMagUnk) {
        this.eqMagUnk = eqMagUnk;
    }

    public BigDecimal getEqMagMb() {
        return eqMagMb;
    }

    public void setEqMagMb(BigDecimal eqMagMb) {
        this.eqMagMb = eqMagMb;
    }

    public BigDecimal getEqMagMs() {
        return eqMagMs;
    }

    public void setEqMagMs(BigDecimal eqMagMs) {
        this.eqMagMs = eqMagMs;
    }

    public BigDecimal getEqMagMw() {
        return eqMagMw;
    }

    public void setEqMagMw(BigDecimal eqMagMw) {
        this.eqMagMw = eqMagMw;
    }

    public BigDecimal getEqMagMl() {
        return eqMagMl;
    }

    public void setEqMagMl(BigDecimal eqMagMl) {
        this.eqMagMl = eqMagMl;
    }

    public BigDecimal getEqMagMta() {
        return eqMagMta;
    }

    public void setEqMagMta(BigDecimal eqMagMta) {
        this.eqMagMta = eqMagMta;
    }

    public BigDecimal getEqMagnatude() {
        return eqMagnatude;
    }

    public void setEqMagnatude(BigDecimal eqMagnatude) {
        this.eqMagnatude = eqMagnatude;
    }

    public BigDecimal getEqMagnatudeRank() {
        return eqMagnatudeRank;
    }

    public void setEqMagnatudeRank(BigDecimal eqMagnatudeRank) {
        this.eqMagnatudeRank = eqMagnatudeRank;
    }

    public Integer getEqDepth() {
        return eqDepth;
    }

    public void setEqDepth(Integer eqDepth) {
        this.eqDepth = eqDepth;
    }

    public BigDecimal getMaxEventRunup() {
        return maxEventRunup;
    }

    public void setMaxEventRunup(BigDecimal maxEventRunup) {
        this.maxEventRunup = maxEventRunup;
    }

    public BigDecimal getTsMtAbe() {
        return tsMtAbe;
    }

    public void setTsMtAbe(BigDecimal tsMtAbe) {
        this.tsMtAbe = tsMtAbe;
    }

    public BigDecimal getTsMtIi() {
        return tsMtIi;
    }

    public void setTsMtIi(BigDecimal tsMtIi) {
        this.tsMtIi = tsMtIi;
    }

    public BigDecimal getTsIntensity() {
        return tsIntensity;
    }

    public void setTsIntensity(BigDecimal tsIntensity) {
        this.tsIntensity = tsIntensity;
    }

    public BigDecimal getDamageMillionsDollars() {
        return damageMillionsDollars;
    }

    public void setDamageMillionsDollars(BigDecimal damageMillionsDollars) {
        this.damageMillionsDollars = damageMillionsDollars;
    }

    public Integer getDamageAmountOrder() {
        return damageAmountOrder;
    }

    public void setDamageAmountOrder(Integer damageAmountOrder) {
        this.damageAmountOrder = damageAmountOrder;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public Integer getHousesDestroyed() {
        return housesDestroyed;
    }

    public void setHousesDestroyed(Integer housesDestroyed) {
        this.housesDestroyed = housesDestroyed;
    }

    public Integer getHousesAmountOrder() {
        return housesAmountOrder;
    }

    public void setHousesAmountOrder(Integer housesAmountOrder) {
        this.housesAmountOrder = housesAmountOrder;
    }

    public String getHousesDescription() {
        return housesDescription;
    }

    public void setHousesDescription(String housesDescription) {
        this.housesDescription = housesDescription;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getDeathsAmountOrder() {
        return deathsAmountOrder;
    }

    public void setDeathsAmountOrder(Integer deathsAmountOrder) {
        this.deathsAmountOrder = deathsAmountOrder;
    }

    public String getDeathsDescription() {
        return deathsDescription;
    }

    public void setDeathsDescription(String deathsDescription) {
        this.deathsDescription = deathsDescription;
    }

    public Integer getInjuries() {
        return injuries;
    }

    public void setInjuries(Integer injuries) {
        this.injuries = injuries;
    }

    public Integer getInjuriesAmountOrder() {
        return injuriesAmountOrder;
    }

    public void setInjuriesAmountOrder(Integer injuriesAmountOrder) {
        this.injuriesAmountOrder = injuriesAmountOrder;
    }

    public String getInjuriesDescription() {
        return injuriesDescription;
    }

    public void setInjuriesDescription(String injuriesDescription) {
        this.injuriesDescription = injuriesDescription;
    }

    public Integer getMissing() {
        return missing;
    }

    public void setMissing(Integer missing) {
        this.missing = missing;
    }

    public Integer getMissingAmountOrder() {
        return missingAmountOrder;
    }

    public void setMissingAmountOrder(Integer missingAmountOrder) {
        this.missingAmountOrder = missingAmountOrder;
    }

    public String getMissingDescription() {
        return missingDescription;
    }

    public void setMissingDescription(String missingDescription) {
        this.missingDescription = missingDescription;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getNumRunup() {
        return numRunup;
    }

    public void setNumRunup(Integer numRunup) {
        this.numRunup = numRunup;
    }

    public BigDecimal getDamageMillionsDollarsTotal() {
        return damageMillionsDollarsTotal;
    }

    public void setDamageMillionsDollarsTotal(BigDecimal damageMillionsDollarsTotal) {
        this.damageMillionsDollarsTotal = damageMillionsDollarsTotal;
    }

    public Integer getDamageAmountOrderTotal() {
        return damageAmountOrderTotal;
    }

    public void setDamageAmountOrderTotal(Integer damageAmountOrderTotal) {
        this.damageAmountOrderTotal = damageAmountOrderTotal;
    }

    public String getDamageTotalDescription() {
        return damageTotalDescription;
    }

    public void setDamageTotalDescription(String damageTotalDescription) {
        this.damageTotalDescription = damageTotalDescription;
    }

    public Integer getHousesDestroyedTotal() {
        return housesDestroyedTotal;
    }

    public void setHousesDestroyedTotal(Integer housesDestroyedTotal) {
        this.housesDestroyedTotal = housesDestroyedTotal;
    }

    public Integer getHousesAmountOrderTotal() {
        return housesAmountOrderTotal;
    }

    public void setHousesAmountOrderTotal(Integer housesAmountOrderTotal) {
        this.housesAmountOrderTotal = housesAmountOrderTotal;
    }

    public String getHousesTotalDescription() {
        return housesTotalDescription;
    }

    public void setHousesTotalDescription(String housesTotalDescription) {
        this.housesTotalDescription = housesTotalDescription;
    }

    public Integer getDeathsTotal() {
        return deathsTotal;
    }

    public void setDeathsTotal(Integer deathsTotal) {
        this.deathsTotal = deathsTotal;
    }

    public Integer getDeathsAmountOrderTotal() {
        return deathsAmountOrderTotal;
    }

    public void setDeathsAmountOrderTotal(Integer deathsAmountOrderTotal) {
        this.deathsAmountOrderTotal = deathsAmountOrderTotal;
    }

    public String getDeathsTotalDescription() {
        return deathsTotalDescription;
    }

    public void setDeathsTotalDescription(String deathsTotalDescription) {
        this.deathsTotalDescription = deathsTotalDescription;
    }

    public Integer getInjuriesTotal() {
        return injuriesTotal;
    }

    public void setInjuriesTotal(Integer injuriesTotal) {
        this.injuriesTotal = injuriesTotal;
    }

    public Integer getInjuriesAmountOrderTotal() {
        return injuriesAmountOrderTotal;
    }

    public void setInjuriesAmountOrderTotal(Integer injuriesAmountOrderTotal) {
        this.injuriesAmountOrderTotal = injuriesAmountOrderTotal;
    }

    public String getInjuriesTotalDescription() {
        return injuriesTotalDescription;
    }

    public void setInjuriesTotalDescription(String injuriesTotalDescription) {
        this.injuriesTotalDescription = injuriesTotalDescription;
    }

    public Integer getMissingTotal() {
        return missingTotal;
    }

    public void setMissingTotal(Integer missingTotal) {
        this.missingTotal = missingTotal;
    }

    public Integer getMissingAmountOrderTotal() {
        return missingAmountOrderTotal;
    }

    public void setMissingAmountOrderTotal(Integer missingAmountOrderTotal) {
        this.missingAmountOrderTotal = missingAmountOrderTotal;
    }

    public String getMissingTotalDescription() {
        return missingTotalDescription;
    }

    public void setMissingTotalDescription(String missingTotalDescription) {
        this.missingTotalDescription = missingTotalDescription;
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

    public Integer getHousesDamaged() {
        return housesDamaged;
    }

    public void setHousesDamaged(Integer housesDamaged) {
        this.housesDamaged = housesDamaged;
    }

    public Integer getHousesDamagedAmountOrder() {
        return housesDamagedAmountOrder;
    }

    public void setHousesDamagedAmountOrder(Integer housesDamagedAmountOrder) {
        this.housesDamagedAmountOrder = housesDamagedAmountOrder;
    }

    public String getHousesDamDescription() {
        return housesDamDescription;
    }

    public void setHousesDamDescription(String housesDamDescription) {
        this.housesDamDescription = housesDamDescription;
    }

    public Integer getHousesDamagedTotal() {
        return housesDamagedTotal;
    }

    public void setHousesDamagedTotal(Integer housesDamagedTotal) {
        this.housesDamagedTotal = housesDamagedTotal;
    }

    public Integer getHousesDamaAmountOrderTotal() {
        return housesDamaAmountOrderTotal;
    }

    public void setHousesDamaAmountOrderTotal(Integer housesDamaAmountOrderTotal) {
        this.housesDamaAmountOrderTotal = housesDamaAmountOrderTotal;
    }

    public String getHousesDamTotalDescription() {
        return housesDamTotalDescription;
    }

    public void setHousesDamTotalDescription(String housesDamTotalDescription) {
        this.housesDamTotalDescription = housesDamTotalDescription;
    }

    public Integer getNumDeposits() {
        return numDeposits;
    }

    public void setNumDeposits(Integer numDeposits) {
        this.numDeposits = numDeposits;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventTsunami)) {
            return false;
        }
        EventTsunami other = (EventTsunami) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cgi.poc.dw.dao.model.EventTsunami[ id=" + id + " ]";
    }
    
}
