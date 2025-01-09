package com.ds4u.schedulingApis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)

@Table(name = "booking_info")
public class BookingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "bookingFor")
    private String bookingFor;

    @Column(name = "insurance_id")
    private String insuranceId;
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "birthDate")
    private String birthDate;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "slot_id")
    private String slotId;

    @Column(name = "sex")
    private String sex;

    @Column(name = "reason")
    private String reason;

    @Column(name = "phoneNumber")
    private String phoneNumber;


    @Column(name = "email" )
    private String email;

    @Column(name = "infoForProvider")
    private String infoForProvider;

    @Column(name = "day")
    private String day;

    @Column(name = "time")
    private String time;


    public String getBookingFor() {
        return bookingFor;
    }

    public String getReason() {
        return reason;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setBookingFor(String bookingFor) {
        this.bookingFor = bookingFor;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAddress() {
        return address;
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

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfoForProvider() {
        return infoForProvider;
    }

    public void setInfoForProvider(String infoForProvider) {
        this.infoForProvider = infoForProvider;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
