package com.ds4u.schedulingApis.service;

import com.ds4u.schedulingApis.configuration.ApiDevConfig;
import com.ds4u.schedulingApis.entity.BookingInfo;
import com.ds4u.schedulingApis.exception.CustomException;
import com.ds4u.schedulingApis.respository.BookingInfoRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealowDevService {

    @Autowired
    private ApiDevConfig apiDevConfig;

    @Autowired
    private BookingInfoRespository bookingInfoRespository;

    //    @Bean
//    public ApiDevConfig getApiConfig(){
//        return new ApiDevConfig();
//    }
    private OkHttpClient client = new OkHttpClient.Builder().build();

    ObjectMapper objectMapper = new ObjectMapper();

    public String getSchedule(@RequestParam String actor, @RequestParam String date, @RequestParam String type, @RequestParam String identifier, @RequestParam Integer actorLocation) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiDevConfig.getApiScheduleDevUrl()).newBuilder();
        urlBuilder.addQueryParameter("actor", actor);
        urlBuilder.addQueryParameter("date", date);
        urlBuilder.addQueryParameter("type", type);
        urlBuilder.addQueryParameter("identifier", identifier);
        urlBuilder.addQueryParameter("actor.location", String.valueOf(actorLocation));

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn())
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiDevConfig.getApiSlotDevUrl()).newBuilder();
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
                .addHeader("Authorization", apiDevConfig.getDevBearerToekn())
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiDevConfig.getApiScheduleDevUrl()).newBuilder();
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
                .addHeader("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn())
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

        urlBuilder = HttpUrl.parse(apiDevConfig.getApiSlotDevUrl()).newBuilder();
        urlBuilder.addQueryParameter("schedule", scheduleId);
        urlBuilder.addQueryParameter("slot-type", slotType);
        urlBuilder.addQueryParameter("start", start);
        urlBuilder.addQueryParameter("_count", String.valueOf(count));

        String slotUrl = urlBuilder.build().toString();
        System.out.println("Slot URL: " + slotUrl); // Debugging line

        Request slotRequest = new Request.Builder()
                .url(slotUrl)
                .get()
                .addHeader("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn())
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

    public String book(BookingInfo bookingInfo) throws IOException {

        URL url = new URL(apiDevConfig.getApiDevBookUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json+fhir");
        conn.setRequestProperty("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn());
        conn.setDoOutput(true);

        System.out.println("conn" + conn);
        Map<String, Object> appointmentBody = new HashMap<>();
        appointmentBody.put("resourceType", "Appointment");

        Map<String, Object> patient = new HashMap<>();
        patient.put("resourceType", "Patient");
        patient.put("id", "patA");

        Map<String, Object> name = new HashMap<>();
        name.put("use", "usual");
        name.put("family", new String[]{bookingInfo.getLastName()});
        name.put("given", new String[]{bookingInfo.getFirstName()});

        patient.put("name", new Map[]{name});

        Map<String, Object> phoneTelecom = new HashMap<>();
        phoneTelecom.put("system", "phone");
        phoneTelecom.put("value", bookingInfo.getPhoneNumber());
        phoneTelecom.put("use", "mobile");

        Map<String, Object> emailTelecom = new HashMap<>();
        emailTelecom.put("system", "email");
        emailTelecom.put("value", bookingInfo.getEmail());
        emailTelecom.put("use", "home");

        patient.put("telecom", new Map[]{phoneTelecom, emailTelecom});
        patient.put("gender", bookingInfo.getSex());
        patient.put("birthDate", bookingInfo.getBirthDate());

        Map<String, Object> coverage = new HashMap<>();
        coverage.put("resourceType", "Coverage");

        Map<String, Object> coding = new HashMap<>();
        coding.put("system", "eCW_insurance_coding_system(Cash/Insurance/Not-Applicable)");
        coding.put("code", "insurance_code");
        coding.put("display", "insurance name");

        Map<String, Object> type = new HashMap<>();
        type.put("coding", new Map[]{coding});
        type.put("text", "insurance name");

        coverage.put("type", type);

        appointmentBody.put("contained", new Map[]{patient, coverage});
        appointmentBody.put("reason", Map.of("text", bookingInfo.getInfoForProvider()));
        appointmentBody.put("description", bookingInfo.getBookingFor());
        appointmentBody.put("slot", new Map[]{Map.of("reference", bookingInfo.getTime())});
        appointmentBody.put("comment", bookingInfo.getInfoForProvider());
        appointmentBody.put("participant", new Map[]{
                Map.of("actor", Map.of("reference", "Practitioner/1234564789"), "required", "required"),
           //     Map.of("actor", Map.of("reference", ""), "required", "required")
        });
        System.out.println("appointmentBody"+appointmentBody);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(appointmentBody);
        System.out.println("jsonBody" + jsonBody);

        // Send the JSON body in the request
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        System.out.println("HTTP Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            bookingInfoRespository.save(bookingInfo);
            System.out.println("BookingInfo saved to database");
            return "booked";
        } else {
            // Handle error response
            System.err.println("Error response: " + conn.getResponseMessage());
            return "failed";
        }
    }

}
