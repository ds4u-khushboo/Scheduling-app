package com.ds4u.schedulingApis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient_info")
public class PatientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "state_id")
    private String stateId;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;
    @Column(name = "email")
    private String email;

    @Column(name = "telecom")
    private String telecom;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<BookingInfo> listOfAppointments = new ArrayList<>();

    @Column(name = "visitReason")
    private String visitReason;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getTelecom() {
        return telecom;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<BookingInfo> getListOfAppointments() {
        return listOfAppointments;
    }

    public void setListOfAppointments(List<BookingInfo> listOfAppointments) {
        this.listOfAppointments = listOfAppointments;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public void setTelecom(String phoneNumber) {
    }
}
