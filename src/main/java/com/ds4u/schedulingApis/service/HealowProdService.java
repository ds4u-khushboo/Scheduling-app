package com.ds4u.schedulingApis.service;


import com.ds4u.schedulingApis.configuration.ApiLiveConfig;
import com.ds4u.schedulingApis.entity.BookingInfo;
import com.ds4u.schedulingApis.exception.CustomException;
import com.ds4u.schedulingApis.respository.BookingInfoRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class HealowProdService {

    @Autowired
    private ApiLiveConfig apiLiveConfig;

    @Autowired
    private BookingInfoRespository bookingInfoRespository;


    @Bean
    public ApiLiveConfig getApiLiveConfig() {
        return new ApiLiveConfig();
    }

    private OkHttpClient client = new OkHttpClient.Builder().build();

    ObjectMapper objectMapper = new ObjectMapper();

    //    private final Cache<String, JsonNode> responseCache;
    @Autowired
    public void FhirApiClient(ApiLiveConfig apiLiveConfig) {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                .build();
        this.objectMapper = new ObjectMapper();
        this.apiLiveConfig = apiLiveConfig;
    }

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
                .addHeader("Authorization", "Bearer " + apiLiveConfig.getApiLiveBearerToken())
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
                .addHeader("Authorization", "Bearer " + apiLiveConfig.getApiLiveBearerToken())
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

    //    @Async
//    @Cacheable
    @Scheduled(fixedRate = 3600)
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
        System.out.println("Slot URL: " + slotUrl);

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

    @CachePut(value = "slots", key = "#date")
    public JsonNode prefetchAndCacheSlots(String actor, String date, String identifier, String type, String location, String start, String slotType, int count) throws IOException {
        // Fetch slot data from third-party API
        return getSchedules(actor, date, identifier, type, location, start, slotType, count);
    }

    @Cacheable(value = "slots", key = "#date")
    public JsonNode getCachedSlots(String actor, String date, String identifier, String type, String location, String start, String slotType, int count) throws IOException {
        // This method will only be called if the cache is empty
        return prefetchAndCacheSlots(actor, date, identifier, type, location, start, slotType, count);
    }

    public JsonNode bookAppointmentLive(BookingInfo bookingInfo) {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(apiLiveConfig.getLiveBookUrl()).newBuilder();
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
                    "                        \"testfamily\"\n" +
                    "                    ],\n" +
                    "                    \"given\": [\n" +
                    "                        \"testgiven\"\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"telecom\": [\n" +
                    "                {\n" +
                    "                    \"system\": \"phone\",\n" +
                    "                    \"value\": \"0000000000\",\n" +
                    "                    \"use\": \"mobile\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"system\": \"email\",\n" +
                    "                    \"value\": \"test@example.com\",\n" +
                    "                    \"use\": \"home\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"gender\": \"male\",\n" +
                    "            \"birthDate\": \"1970-01-01\",\n" +
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
                    "    \"id\": \"\",\n" +
                    "    \"status\": \"proposed\",\n" +
                    "    \"reason\": {\n" +
                    "        \"text\": \"The reason that this appointment is being scheduled. (e.g. Regular checkup)\"\n" +
                    "    },\n" +
                    "    \"description\": \"The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list.\",\n" +
                    "    \"slot\": [\n" +
                    "        {\n" +
                    "            \"reference\": \"ad8aab00-7df7-4723-b085-857054b55c6d\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"comment\": \"Additional comments about the appointment.\",\n" +
                    "    \"participant\": [\n" +
                    "        {\n" +
                    "            \"actor\": {\n" +
                    "                \"reference\": \"Practitioner/1234564789\"\n" +
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

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(90, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            okhttp3.RequestBody body = RequestBody.create(
                    jsonInputString,
                    MediaType.get("application/json+fhir")
            );
            System.out.println("body" + body.toString());

            Request bookRequest = new Request.Builder()
                    .url(bookUrl)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiLiveConfig.getApiLiveBearerToken())
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
//                        return new ResponseEntity<>(errorNode, HttpStatus.BAD_REQUEST).getBody();

                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

//    catch (IOException e) {
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
//        String baseUrl = apiLiveConfig.getLiveBookUrl();
//            URL url = new URL(urlBuilder);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json+fhir");
//            conn.setRequestProperty("Authorization", "Bearer " + apiLiveConfig.getApiLiveBearerToken());
//            conn.setDoOutput(true);
//
//            // Construct JSON payload
//
//            try (OutputStream os = conn.getOutputStream()) {
//                byte[] input = jsonInputString.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_CREATED) {
//                bookingInfoRespository.save(bookingInfo);
//                return "BookingInfo saved to database";
//            } else {
//                // Print error response
//                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
//                    StringBuilder response = new StringBuilder();
//                    String responseLine;
//                    while ((responseLine = br.readLine()) != null) {
//                        response.append(responseLine.trim());
//                    }
//                    return responseCode + response.toString();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "success";


}