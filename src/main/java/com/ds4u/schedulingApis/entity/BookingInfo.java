package com.ds4u.schedulingApis.entity;

import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)

@Table(name = "booking_info")
public class BookingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "bookingFor")
    private String bookingFor;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "birthDate")
    private String birthDate;

    private String resourceType;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @Column(name = "sex")
    private String sex;

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

    public void setBookingFor(String bookingFor) {
        this.bookingFor = bookingFor;
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
