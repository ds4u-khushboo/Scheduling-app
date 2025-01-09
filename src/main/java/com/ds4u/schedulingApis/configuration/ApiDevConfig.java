package com.ds4u.schedulingApis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConditionalOnProperty(prefix = "profiles", name = "active")
@Profile("dev")
public class ApiDevConfig {

    @Value("${api_schedule_dev_url}")
    private String apiScheduleDevUrl;

    @Value("${api_slot_dev_url}")
    private String apiSlotDevUrl;

    @Value("${api_dev_book_url}")
    private String apiDevBookUrl;

    @Value("${api_dev_bearer_token}")
    private String devBearerToekn;

    @Value("${actor_npi_dev}")
    private String actorDevNpi;



    @Value("${actor_dev_location}")
    private String actorDevLocation;



    public String getApiScheduleDevUrl() {
        return apiScheduleDevUrl;
    }

    public void setApiScheduleDevUrl(String apiScheduleDevUrl) {
        this.apiScheduleDevUrl = apiScheduleDevUrl;
    }

    public String getApiSlotDevUrl() {
        return apiSlotDevUrl;
    }

    public void setApiSlotDevUrl(String apiSlotDevUrl) {
        this.apiSlotDevUrl = apiSlotDevUrl;
    }

    public String getApiDevBookUrl() {
        return apiDevBookUrl;
    }

    public void setApiDevBookUrl(String apiDevBookUrl) {
        this.apiDevBookUrl = apiDevBookUrl;
    }

    public String getDevBearerToekn() {
        return devBearerToekn;
    }

    public void setDevBearerToekn(String devBearerToekn) {
        this.devBearerToekn = devBearerToekn;
    }

//    public String getApiScheduleLiveUrl() {
//        return apiScheduleLiveUrl;
//    }
//
//    public void setApiScheduleLiveUrl(String apiScheduleLiveUrl) {
//        this.apiScheduleLiveUrl = apiScheduleLiveUrl;
//    }
//
//    public String getSlotLiveUrl() {
//        return slotLiveUrl;
//    }
//
//    public void setSlotLiveUrl(String slotLiveUrl) {
//        this.slotLiveUrl = slotLiveUrl;
//    }
//
//    public String getApiLiveBearerToken() {
//        return apiLiveBearerToken;
//    }
//
//    public void setApiLiveBearerToken(String apiLiveBearerToken) {
//        this.apiLiveBearerToken = apiLiveBearerToken;
//    }

    public String getActorDevNpi() {
        return actorDevNpi;
    }

    public void setActorDevNpi(String actorDevNpi) {
        this.actorDevNpi = actorDevNpi;
    }

//    public String getActorLiveNpi() {
//        return actorLiveNpi;
//    }
//
//    public void setActorLiveNpi(String actorLiveNpi) {
//        this.actorLiveNpi = actorLiveNpi;
//    }

    public String getActorDevLocation() {
        return actorDevLocation;
    }

    public void setActorDevLocation(String actorDevLocation) {
        this.actorDevLocation = actorDevLocation;
    }

//    public String getActorLiveLocation() {
//        return actorLiveLocation;
//    }
//
//    public void setActorLiveLocation(String actorLiveLocation) {
//        this.actorLiveLocation = actorLiveLocation;
//    }
}