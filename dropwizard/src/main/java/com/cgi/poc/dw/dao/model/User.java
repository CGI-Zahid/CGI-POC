/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao.model;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;

import com.cgi.poc.dw.util.LoginValidationGroup;
import com.cgi.poc.dw.util.PasswordType;
import com.cgi.poc.dw.util.PersistValidationGroup;
import com.cgi.poc.dw.util.RestValidationGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author dawna.floyd
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedNativeQueries({
    @NamedNativeQuery(name = "getGeoWithinRadius", query = "SELECT "
        + "  *, ( "
        + "    3959 * acos ( "
        + "      cos ( radians( :lat) ) "
        + "      * cos( radians( latitude ) ) "
        + "      * cos( radians( longitude ) - radians( :lng ) ) "
        + "      + sin ( radians( :lat ) ) "
        + "      * sin( radians( latitude ) ) "
        + "    ) "
        + "  ) AS distance FROM `user` HAVING distance < :radius AND `role`= 'RESIDENT' ORDER BY distance",
        resultClass = User.class)})
public class User implements Serializable, Principal {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	@JsonIgnore
	private Long id;

	@Basic(optional = false)
	@NotNull()
	@Size(min = 1, max = 65)
	@Column(name = "first_name")
	private String firstName;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 65)
	@Column(name = "last_name")
	private String lastName;

	// pattern checks for XXX@YYY.com
	// generated regex
	@ApiModelProperty(value = "Validates for standard email format:  XXX@YYY.ZZZ. No whitespace allowed ", required = true)
	@Pattern(groups = { Default.class,
			LoginValidationGroup.class }, regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Invalid email address.")
	@Basic(optional = false)
	@NotNull(groups = { Default.class, LoginValidationGroup.class })
	@Size(min = 1, max = 150, groups = { Default.class, LoginValidationGroup.class })
	@Column(name = "email")
	private String email;

	// TODO: modify password validation
	@Basic(optional = false)
	@NotNull(message = "is missing", groups = { Default.class, LoginValidationGroup.class })
	@Size(min = 2, max = 500, message = "must be at least 2 characters in length.")
	@Column(name = "password")
	@PasswordType(message = "must be greater that 2 character, contain no whitespace, and have at least one number and one letter.", groups = {
			RestValidationGroup.class, LoginValidationGroup.class })
	private String password;

	// if the field contains phone or fax number consider using this annotation to
	// enforce field validation
	// @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
	// message="Invalid phone/fax format, should be as xxx-xxx-xxxx")
	@Basic(optional = false)
	@NotNull
	@Size(min = 10, max = 10)
	@Column(name = "phone")
	private String phone;

	@Basic(optional = false)
	@NotNull
	@Size(min = 4, max = 250, message = "must be a valid street name")
	@Column(name = "address")
	private String requiredStreet;

	@Basic(optional = true)
	@Column(name = "address_additional_info")
	private String optionalStreet;

	@Basic(optional = false)
	@NotNull
	@Size(min = 2, max = 150, message = "must be a valid street name")
	@Column(name = "city")
	private String city;

	@Basic(optional = false)
	@NotNull
	@Column(name = "state")
	private String state;

	@Basic(optional = false)
	@NotNull
	@Pattern(regexp = "\\d{5}", message = "is invalid.")
	@Column(name = "zip_code")
	private String zipCode;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 8)
	@Column(name = "role")
	@JsonIgnore
	private String role;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	// @Basic(optional = false)
	@NotNull(groups = { PersistValidationGroup.class })
	@Column(name = "latitude")
	@JsonIgnore
	private Double latitude;

	// @Basic(optional = false)
	@NotNull(groups = { PersistValidationGroup.class })
	@Column(name = "longitude")
	@JsonIgnore
	private Double longitude;

	@OneToMany(mappedBy = "userId", fetch = FetchType.EAGER, orphanRemoval = true)
	@NotNull
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	private Set<UserNotificationType> notificationType;

	public User() {
	}

	public User(Long id) {
		this.id = id;
	}

	public User(Long id, String firstName, String lastName, String email, String password, String phone,
			String requiredStreet, String optionalStreet, String city, String state, String zipCode,
			String role, double latitude, double longitude) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.requiredStreet = requiredStreet;
		this.optionalStreet = optionalStreet;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.role = role;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Set<UserNotificationType> getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(Set<UserNotificationType> notificationType) {
		this.notificationType = notificationType;
	}

	public String getRequiredStreet() {
		return requiredStreet;
	}

	public void setRequiredStreet(String requiredStreet) {
		this.requiredStreet = requiredStreet;
	}

	public String getOptionalStreet() {
		return optionalStreet;
	}

	public void setOptionalStreet(String optionalStreet) {
		this.optionalStreet = optionalStreet;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof User)) {
			return false;
		}
		User other = (User) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.cgi.poc.dw.dao.model.User[ id=" + id + " ]";
	}

	/*
	 * dropwizard requires to implement principal for authentication which
	 * implements getName()
	 */
	@JsonIgnore
	@Override
	public String getName() {
		return email;
	}

}
