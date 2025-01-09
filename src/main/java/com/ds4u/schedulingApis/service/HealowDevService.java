package com.ds4u.schedulingApis.service;

import com.ds4u.schedulingApis.configuration.ApiDevConfig;
import com.ds4u.schedulingApis.entity.BookingInfo;
import com.ds4u.schedulingApis.exception.CustomException;
import com.ds4u.schedulingApis.respository.BookingInfoRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Service
public class HealowDevService {

    @Autowired
    private ApiDevConfig apiDevConfig;
    @Autowired
    private BookingInfoRespository bookingInfoRespository;

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
                .addHeader("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn())
                //.addHeader("Cookie", "JSESSIONID=78C5A71D81FA9FF3F943D799C9923F0F; ApplicationGatewayAffinity=cef1f429a0207a8fd583686fb86a5ca3; ApplicationGatewayAffinityCORS=cef1f429a0207a8fd583686fb86a5ca3; SERVERID=app02_8003")
                .build();
        System.out.println("request--" + request);
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            System.out.println("response--" + response);
            String responseBody = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode originalResponse = mapper.readTree(responseBody);

            // Check if there are existing entries
            ArrayNode entries;
            if (originalResponse.has("entry")) {
                entries = (ArrayNode) originalResponse.get("entry");
            } else {
                entries = mapper.createArrayNode();
            }

            // Create the new entry
            ObjectNode newEntry = mapper.createObjectNode();
            newEntry.put("fullUrl", "http://connect4.healow.com/apps/api/v1/fhir/BEHDAD/fhir/Schedule/c4554eb8-ee68-4a2c-bff3-eceddcf8ac8a");

            ObjectNode resource = mapper.createObjectNode();
            resource.put("resourceType", "Schedule");
            resource.put("id", "c4554eb8-ee68-4a2c-bff3-eceddcf8ac8a");

            ObjectNode actor = mapper.createObjectNode();
            actor.put("reference", "1275534000");

            resource.set("actor", actor);

            ObjectNode planningHorizon = mapper.createObjectNode();
            planningHorizon.put("start", "2024-07-26T00:00:00-04:00");
            planningHorizon.put("end", "2024-07-26T00:00:00-04:00");

            resource.set("planningHorizon", planningHorizon);

            newEntry.set("resource", resource);

            entries.add(newEntry);
            ((ObjectNode) originalResponse).set("entry", entries);
            ((ObjectNode) originalResponse).put("total", entries.size());
            String updatedResponseBody = mapper.writeValueAsString(originalResponse);
            System.out.println(updatedResponseBody);
            return updatedResponseBody;

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
            ObjectMapper mapper = new ObjectMapper();
            JsonNode originalResponse = mapper.readTree(slotResponseBody);

            ArrayNode entries;
            if (originalResponse.has("entry")) {
                entries = (ArrayNode) originalResponse.get("entry");
            } else {
                entries = mapper.createArrayNode();
            }

            // Create the new entry
            ObjectNode newEntry = mapper.createObjectNode();
            newEntry.put("fullUrl", "http://connect4.healow.com/apps/api/v1/fhir/BEHDAD/fhir/Schedule/c4554eb8-ee68-4a2c-bff3-eceddcf8ac8a");

            ObjectNode resource = mapper.createObjectNode();
            resource.put("resourceType", "Schedule");
            resource.put("id", "c4554eb8-ee68-4a2c-bff3-eceddcf8ac8a");

            ObjectNode actors = mapper.createObjectNode();
            actors.put("reference", "1275534000");

            resource.set("actor", actors);

            ObjectNode planningHorizon = mapper.createObjectNode();
            planningHorizon.put("start", "2024-07-26T00:00:00-04:00");
            planningHorizon.put("end", "2024-07-26T00:00:00-04:00");

            resource.set("planningHorizon", planningHorizon);

            newEntry.set("resource", resource);

            // Add the new entry to the entries array
            entries.add(newEntry);

            // Update the original response with the new entries array and the other required fields
            ((ObjectNode) originalResponse).set("entry", entries);
            ((ObjectNode) originalResponse).put("total", entries.size());

            // Return the updated response as a string
            String updatedResponseBody = mapper.writeValueAsString(originalResponse);
            System.out.println(updatedResponseBody);
            //return updatedResponseBody;
            return objectMapper.readTree(updatedResponseBody);
        }
    }
//    @CachePut(value = "schedules", key = "#root.methodName")
//    public JsonNode prefetchAndCacheData() {
//        // Fetch data from third-party API
//        JsonNode data = client.();
//        return data;
//    }
//
//    @Cacheable(value = "schedules", key = "#root.methodName")
//    public JsonNode getCachedData() {
//        // This method will only be called if the cache is empty
//        return prefetchAndCacheData();
//    }

    private String extractScheduleId(String responseBody) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(responseBody);
        // Check if the "entry" array exists and has elements
        if (root.path("entry").isArray() && root.path("entry").size() > 0) {
            JsonNode firstEntry = root.path("entry").get(0);
            return firstEntry.path("resource").path("id").asText();
        }
        return null;
    }

    //public String book(BookingInfo bookingInfo) throws IOException {
    public JsonNode bookAppointmentDev(BookingInfo bookingInfo) throws IOException {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(apiDevConfig.getApiDevBookUrl()).newBuilder();
            String bookUrl = urlBuilder.build().toString();

            String jsonInputString = "{\n" +
                    "    \"resourceType\": \"Appointment\",\n" +
                    "    \"contained\": [\n" +
                    "        {\n" +
                    "            \"resourceType\": \"Patient\",\n" +
                    "            \"id\": \"patA\",\n" +
                    "            \"name\": [\n" +
                    "                {\n" +
                    "                    \"use\": \"usual\",\n" +
                    "                    \"family\": [\n" +
                    "                        \"" + bookingInfo.getLastName() + "\"\n" +
                    "                    ],\n" +
                    "                    \"given\": [\n" +
                    "                        \"" + bookingInfo.getFirstName() + "\"\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"telecom\": [\n" +
                    "                {\n" +
                    "                    \"system\": \"phone\",\n" +
                    "                    \"value\": \"" + bookingInfo.getPhoneNumber() + "\",\n" +
                    "                    \"use\": \"mobile\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"system\": \"email\",\n" +
                    "                    \"value\": \"" + bookingInfo.getEmail() + "\",\n" +
                    "                    \"use\": \"home\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"gender\": \"" + bookingInfo.getSex() + "\",\n" +
                    "            \"birthDate\": \"" + bookingInfo.getBirthDate() + "\",\n" +
                    "            \"address\": [\n" +
                    "                {\n" +
                    "                    \"use\": \"home\",\n" +
                    "                    \"type\": \"both\",\n" +
                    "                    \"text\": \"132, My Street, Kingston, NY, 12401, US\",\n" +
                    "                    \"line\": [\n" +
                    "                        \"132\",\n" +
                    "                        \"My Street\"\n" +
                    "                    ],\n" +
                    "                    \"city\": \"Kingston\",\n" +
                    "                    \"state\": \"NY\",\n" +
                    "                    \"postalCode\": \"12401\",\n" +
                    "                    \"country\": \"US\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"resourceType\": \"Coverage\",\n" +
                    "            \"type\": {\n" +
                    "                \"coding\": [\n" +
                    "                    {\n" +
                    "                        \"system\": \"eCW_insurance_coding_system(Cash/Insurance/Not-Applicable)\",\n" +
                    "                        \"code\": \"insurance_code\",\n" +
                    "                        \"display\": \"insurance name\"\n" +
                    "                    }\n" +
                    "                ],\n" +
                    "                \"text\": \"insurance name\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"status\": \"proposed\",\n" +
                    "    \"reason\": {\n" +
                    "        \"text\": \"" + bookingInfo.getReason() + "\"\n" +
                    "    },\n" +
                    "    \"description\": \"The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list.\",\n" +
                    "    \"slot\": [\n" +
                    "        {\n" +
                    "            \"reference\": \"" + bookingInfo.getSlotId() + "\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"comment\": \"Additional comments about the appointment.\",\n" +
                    "    \"participant\": [\n" +
                    "        {\n" +
                    "            \"actor\": {\n" +
                    "                \"reference\": \"Practitioner/" + apiDevConfig.getActorDevNpi() + "\"\n" +
                    "            },\n" +
                    "            \"required\": \"required\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"actor\": {\n" +
                    "                \"reference\": \"#patA\"\n" +
                    "            },\n" +
                    "            \"required\": \"required\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            System.out.println("jsonInputString___\n" + jsonInputString);

            OkHttpClient client = new OkHttpClient();

            okhttp3.RequestBody body = RequestBody.create(
                    jsonInputString,
                    MediaType.get("application/json+fhir")
            );
            System.out.println("body" + body.toString());

            Request bookRequest = new Request.Builder()
                    .url(bookUrl)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn())
                    .build();

            try (Response response = client.newCall(bookRequest).execute()) {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    bookingInfoRespository.save(bookingInfo);
                    return objectMapper.readTree(responseBody);
                } else {
                    try {
                        return objectMapper.readTree(responseBody);
                    } catch (JsonProcessingException e) {
                        ObjectNode errorNode = objectMapper.createObjectNode();
                        errorNode.put("error", "Received non-JSON response");
                        errorNode.put("response", responseBody);
                        return errorNode;
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
//        } catch (IOException e) {
//            e.printStackTrace();
//            ObjectNode errorNode = objectMapper.createObjectNode();
//            errorNode.put("error", "Error: " + e.getMessage());
//            return errorNode;
//        } catch (Exception e) {
//            e.printStackTrace();
//            ObjectNode errorNode = objectMapper.createObjectNode();
//            errorNode.put("error", "Error: " + e.getMessage());
//            return errorNode;
//        }
//        URL url = new URL(apiDevConfig.getApiDevBookUrl());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json+fhir");
//        conn.setRequestProperty("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn());
//        conn.setDoOutput(true);
//
//        System.out.println("conn" + conn);
//        Map<String, Object> appointmentBody = new HashMap<>();
//        appointmentBody.put("resourceType", "Appointment");
//
//        Map<String, Object> patient = new HashMap<>();
//        patient.put("resourceType", "Patient");
//        patient.put("id", "patA");
//
//        Map<String, Object> name = new HashMap<>();
//        name.put("use", "usual");
//        name.put("family", new String[]{bookingInfo.getLastName()});
//        name.put("given", new String[]{bookingInfo.getFirstName()});
//
//        patient.put("name", new Map[]{name});
//
//        Map<String, Object> phoneTelecom = new HashMap<>();
//        phoneTelecom.put("system", "phone");
//        phoneTelecom.put("value", bookingInfo.getPhoneNumber());
//        phoneTelecom.put("use", "mobile");
//
//        Map<String, Object> emailTelecom = new HashMap<>();
//        emailTelecom.put("system", "email");
//        emailTelecom.put("value", bookingInfo.getEmail());
//        emailTelecom.put("use", "home");
//
//        patient.put("telecom", new Map[]{phoneTelecom, emailTelecom});
//        patient.put("gender", bookingInfo.getSex());
//        patient.put("birthDate", bookingInfo.getBirthDate());
//
//        Map<String, Object> coverage = new HashMap<>();
//        coverage.put("resourceType", "Coverage");
//
//        Map<String, Object> coding = new HashMap<>();
//        coding.put("system", "eCW_insurance_coding_system(Cash/Insurance/Not-Applicable)");
//        coding.put("code", "insurance_code");
//        coding.put("display", "insurance name");
//
//        Map<String, Object> type = new HashMap<>();
//        type.put("coding", new Map[]{coding});
//        type.put("text", "insurance name");
//
//        coverage.put("type", type);
//
//        appointmentBody.put("contained", new Map[]{patient, coverage});
//        appointmentBody.put("reason", Map.of("text", bookingInfo.getInfoForProvider()));
//        appointmentBody.put("description", bookingInfo.getBookingFor());
//        appointmentBody.put("slot", new Map[]{Map.of("reference", bookingInfo.getTime())});
//        appointmentBody.put("comment", bookingInfo.getInfoForProvider());
//        appointmentBody.put("participant", new Map[]{
//                Map.of("actor", Map.of("reference", "Practitioner/1234564789"), "required", "required"),
//           //     Map.of("actor", Map.of("reference", ""), "required", "required")
//        });
//        System.out.println("appointmentBody"+appointmentBody);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonBody = objectMapper.writeValueAsString(appointmentBody);
//        System.out.println("jsonBody" + jsonBody);
//
//        // Send the JSON body in the request
//        try (OutputStream os = conn.getOutputStream()) {
//            byte[] input = jsonBody.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        int responseCode = conn.getResponseCode();
//        System.out.println("HTTP Response Code: " + responseCode);
//
//        if (responseCode == HttpURLConnection.HTTP_CREATED) {
//            bookingInfoRespository.save(bookingInfo);
//            System.out.println("BookingInfo saved to database");
//            return "booked";
//        } else {
//            // Handle error response
//            InputStream errorStream = conn.getErrorStream();
//            String errorResponse = "";
//            if (errorStream != null) {
//                try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream))) {
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        errorResponse += line;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.err.println("Error response: " + errorResponse);
//            return "failed";
//        }
//    }

}

