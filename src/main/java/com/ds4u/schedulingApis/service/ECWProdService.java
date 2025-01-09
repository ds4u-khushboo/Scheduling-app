package com.ds4u.schedulingApis.service;

import com.ds4u.schedulingApis.configuration.ApiDevConfig;
import com.ds4u.schedulingApis.response.ResultResponse;
import com.ds4u.schedulingApis.response.ScheduleResponse;
import com.ds4u.schedulingApis.response.SlotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ECWProdService {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    private ApiDevConfig apiDevConfig;

    public ResultResponse getCombinedResponse(String actor, String date, String location, String slotType, String start, int count) {
        try {
            String scheduleUrl = UriComponentsBuilder.fromHttpUrl(apiDevConfig.getApiScheduleDevUrl())
                    .queryParam("actor", actor)
                    .queryParam("date", date)
                    .queryParam("actor.location", location)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiDevConfig.getDevBearerToekn());

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ScheduleResponse> scheduleResponseEntity = restTemplate.getForEntity(
                    scheduleUrl
                    ,ScheduleResponse.class,
                  entity
            );

            if (!scheduleResponseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to retrieve Schedule data. Status code: " + scheduleResponseEntity.getStatusCodeValue());
            }

            ScheduleResponse scheduleResponse = scheduleResponseEntity.getBody();

            String scheduleId = scheduleResponse.getEntry().get(0).getResource().getId();

            String slotUrl = UriComponentsBuilder.fromHttpUrl(apiDevConfig.getApiSlotDevUrl())
                    .queryParam("schedule", scheduleId)
                    .queryParam("slot-type", slotType)
                    .queryParam("start", start)
                    .queryParam("_count", count)
                    .toUriString();

            ResponseEntity<SlotResponse> slotResponseEntity = restTemplate.getForEntity(
                    slotUrl,
                    SlotResponse.class,entity
            );

            if (!slotResponseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to retrieve Slot data. Status code: " + slotResponseEntity.getStatusCodeValue());
            }

            SlotResponse slotResponse = slotResponseEntity.getBody();

            ResultResponse combinedResponse = new ResultResponse();
            combinedResponse.setScheduleResponse(scheduleResponse);
            combinedResponse.setSlotResponse(slotResponse);

            return combinedResponse;

        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("401 Unauthorized error occurred: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve data from APIs", e);
        }
    }
}
