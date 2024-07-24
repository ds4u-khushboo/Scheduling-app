package com.ds4u.schedulingApis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provider")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "providerId")
    private Long providerId;

    @Column(name = "providerName")
    private String name;

    @OneToMany
    @JsonIgnore
    List<PatientInfo> patientList = new ArrayList<>();
//    @OneToMany
//    @JsonIgnore
//    List<BookingInfo> listOfAppointments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Insurance insurance;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Column(name = "imageUrl")
    private String imageurl;
    @Column(name = "address")
    private String address;
    @Column(name = "language")
    private String language;

    @Column(name = "speciality")
    private String specialty;

    public Provider(String name, String specialty) {
        this.name = name;
        this.specialty = specialty;
    }

    public Provider() {

    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

//    public List<BookingInfo> getListOfAppointments() {
//        return listOfAppointments;
//    }

//    public void setListOfAppointments(List<BookingInfo> listOfAppointments) {
//        this.listOfAppointments = listOfAppointments;
//    }
}
