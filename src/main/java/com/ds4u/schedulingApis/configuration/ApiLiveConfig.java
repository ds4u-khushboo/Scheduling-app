package com.ds4u.schedulingApis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConditionalOnProperty(prefix = "profiles", name = "active")
@Profile("prod")
public class ApiLiveConfig {
    @Value("${api_schedule_live_url}")
    private String apiScheduleLiveUrl;

    @Value("${api_slot_live_url}")
    private String slotLiveUrl;

    @Value("${api_book_live_url}")
    private String liveBookUrl;

    @Value("${api_live_bearer_token}")
    private String apiLiveBearerToken;

    @Value("${actor_live_npi}")
    private String actorLiveNpi;

    @Value("${actor_live_location}")
    private String actorLiveLocation;

    public String getApiScheduleLiveUrl() {
        return apiScheduleLiveUrl;
    }

    public void setApiScheduleLiveUrl(String apiScheduleLiveUrl) {
        this.apiScheduleLiveUrl = apiScheduleLiveUrl;
    }

    public String getSlotLiveUrl() {
        return slotLiveUrl;
    }

    public String getLiveBookUrl() {
        return liveBookUrl;
    }

    public void setLiveBookUrl(String liveBookUrl) {
        this.liveBookUrl = liveBookUrl;
    }

    public void setSlotLiveUrl(String slotLiveUrl) {
        this.slotLiveUrl = slotLiveUrl;
    }

    public String getApiLiveBearerToken() {
        return apiLiveBearerToken;
    }

    public void setApiLiveBearerToken(String apiLiveBearerToken) {
        this.apiLiveBearerToken = apiLiveBearerToken;
    }

    public String getActorLiveNpi() {
        return actorLiveNpi;
    }

    public void setActorLiveNpi(String actorLiveNpi) {
        this.actorLiveNpi = actorLiveNpi;
    }

    public String getActorLiveLocation() {
        return actorLiveLocation;
    }

    public void setActorLiveLocation(String actorLiveLocation) {
        this.actorLiveLocation = actorLiveLocation;
    }
}
