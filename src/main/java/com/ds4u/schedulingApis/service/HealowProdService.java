package com.ds4u.schedulingApis.service;


import com.ds4u.schedulingApis.configuration.ApiLiveConfig;
import com.ds4u.schedulingApis.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Service
public class HealowProdService {

    @Autowired
    private ApiLiveConfig apiLiveConfig;

    @Bean
    public ApiLiveConfig getApiLiveConfig(){
        return new ApiLiveConfig();
    }
    private OkHttpClient client = new OkHttpClient.Builder().build();

    ObjectMapper objectMapper=new ObjectMapper();
    public String getSchedule(@RequestParam String actor, @RequestParam String date, @RequestParam String type, @RequestParam String identifier, @RequestParam Integer actorLocation) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiLiveConfig.getApiScheduleLiveUrl()).newBuilder();
        urlBuilder.addQueryParameter("actor", actor);
        urlBuilder.addQueryParameter("date", date);
        urlBuilder.addQueryParameter("type", type);
        urlBuilder.addQueryParameter("identifier", identifier);
        urlBuilder.addQueryParameter("actor.location", String.valueOf(actorLocation));

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer "+ apiLiveConfig.getApiLiveBearerToken())
                // .addHeader("Cookie", "JSESSIONID=78C5A71D81FA9FF3F943D799C9923F0F; ApplicationGatewayAffinity=cef1f429a0207a8fd583686fb86a5ca3; ApplicationGatewayAffinityCORS=cef1f429a0207a8fd583686fb86a5ca3; SERVERID=app02_8003")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            // Log the specific exception details
            System.err.println("IOException while executing FHIR request:");
            e.printStackTrace();
            throw e;
        }
    }

    public String getSlots(@RequestParam String schedule, String slotType, String start, Integer count) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiLiveConfig.getSlotLiveUrl()).newBuilder();
        urlBuilder.addQueryParameter("schedule", schedule);
        urlBuilder.addQueryParameter("slot-type", slotType);
        urlBuilder.addQueryParameter("start", start);
        if (count != null) {
            urlBuilder.addQueryParameter("_count", String.valueOf(count));
        }
        String sloturl = urlBuilder.build().toString();
        System.out.println("sloturl---" + sloturl);

        Request request = new Request.Builder()
                .url(sloturl)
                .get()
                .addHeader("Authorization","Bearer "+ apiLiveConfig.getApiLiveBearerToken())
                //.addHeader("Cookie", "JSESSIONID=78C5A71D81FA9FF3F943D799C9923F0F; ApplicationGatewayAffinity=cef1f429a0207a8fd583686fb86a5ca3; ApplicationGatewayAffinityCORS=cef1f429a0207a8fd583686fb86a5ca3; SERVERID=app02_8003")
                .build();
        System.out.println("request--" + request);
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            System.out.println("response--" + response);
            String responseBody = response.body().string();
            System.out.println("responseBody____" + responseBody);
            return responseBody;

        } catch (IOException e) {
            System.err.println("IOException while executing FHIR request:");
            e.printStackTrace();
            throw e;
        }
    }
    public JsonNode getSchedules(@RequestParam String actor, String date, String identifier, String type, String location, String slotType, String start, int count) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiLiveConfig.getApiScheduleLiveUrl()).newBuilder();
        urlBuilder.addQueryParameter("actor", actor);
        urlBuilder.addQueryParameter("date", date);
        urlBuilder.addQueryParameter("actor.location", location);
        urlBuilder.addQueryParameter("identifier", identifier);
        urlBuilder.addQueryParameter("type", type);

        String scheduleUrl = urlBuilder.build().toString();
        System.out.println("Schedule URL: " + scheduleUrl);

        Request scheduleRequest = new Request.Builder()
                .url(scheduleUrl)
                .get()
                .addHeader("Authorization", "Bearer " + apiLiveConfig.getApiLiveBearerToken())
                .build();
        System.out.println("scheduleRequest" + scheduleRequest);

        String scheduleId;
        try (Response scheduleResponse = client.newCall(scheduleRequest).execute()) {
            if (!scheduleResponse.isSuccessful()) {
                throw new IOException("Unexpected code " + scheduleResponse + " with body " + scheduleResponse.body().string());
            }
            String scheduleResponseBody = scheduleResponse.body().string();
            System.out.println("Schedule Response: " + scheduleResponseBody); // Debugging line
            scheduleId = extractScheduleId(scheduleResponseBody);
            if (scheduleId == null) {
                throw new CustomException("No schedule available for the specified date.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        urlBuilder = HttpUrl.parse(apiLiveConfig.getSlotLiveUrl()).newBuilder();
        urlBuilder.addQueryParameter("schedule", scheduleId);
        urlBuilder.addQueryParameter("slot-type", slotType);
        urlBuilder.addQueryParameter("start", start);
        urlBuilder.addQueryParameter("_count", String.valueOf(count));

        String slotUrl = urlBuilder.build().toString();
        System.out.println("Slot URL: " + slotUrl); // Debugging line

        Request slotRequest = new Request.Builder()
                .url(slotUrl)
                .get()
                .addHeader("Authorization", "Bearer " + apiLiveConfig.getApiLiveBearerToken())
                .build();

        try (Response slotResponse = client.newCall(slotRequest).execute()) {
            if (!slotResponse.isSuccessful()) {
                throw new IOException("Unexpected code " + slotResponse + " with body " + slotResponse.body().string());
            }
            String slotResponseBody = slotResponse.body().string();

            return objectMapper.readTree(slotResponseBody);
        }
    }
    private String extractScheduleId(String responseBody) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(responseBody);
        // Check if the "entry" array exists and has elements
        if (root.path("entry").isArray() && root.path("entry").size() > 0) {
            JsonNode firstEntry = root.path("entry").get(0);
            return firstEntry.path("resource").path("id").asText();
        }
        return null;
    }
    
}
