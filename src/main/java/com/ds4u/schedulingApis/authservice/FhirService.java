package com.ds4u.schedulingApis.authservice;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Schedule;
import ca.uhn.fhir.model.dstu2.resource.Slot;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import com.ds4u.schedulingApis.controller.AuthHeaderInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FhirService {

    private final FhirContext fhirContext;
    public IGenericClient client;

    protected ObjectMapper objectMapper = new ObjectMapper();

//    private final ExecutorService executorService;

    private OkHttpClient clients = new OkHttpClient.Builder().build();


    @Autowired
    public FhirService(AuthHeaderInterceptor authHeaderInterceptor) {
        fhirContext = FhirContext.forDstu2();

        client = fhirContext.newRestfulGenericClient("https://azuhealow-preprod.healow.com/apps/api/v1/fhir/IFDECD/dstu2");
        fhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        client.registerInterceptor(new LoggingInterceptor(false));
        client.registerInterceptor(authHeaderInterceptor);
    }

    public String searchSchedulesByActor(String actor, String type, String date, String identifier, String actorLocation) {
        try {
            Bundle responseBundle = client
                    .search()
                    .forResource(Schedule.class)
                    .where(Schedule.ACTOR.hasId(actor))
                    .and(Schedule.DATE.exactly().day(date))
                    .and(Schedule.TYPE.exactly().code(type))
                    .and(Schedule.IDENTIFIER.exactly().identifier(identifier))
                    .and(new StringClientParam("actor.location").matches().value(actorLocation))
                    .returnBundle(Bundle.class).accept("application/json").execute();
            System.out.println("responseBundle" + responseBundle);
            List<String> scheduleJsonList = new ArrayList<>();

            for (Bundle.Entry entry : responseBundle.getEntry()) {
                if (entry.getResource() instanceof Schedule) {
                    Schedule schedule = (Schedule) entry.getResource();
                    String scheduleJson = client.getFhirContext().newJsonParser().encodeResourceToString(schedule);
                    scheduleJsonList.add(scheduleJson);
                    System.out.println("scheduleJson: " + scheduleJson);
                }
            }

            String bundleJson = fhirContext.newJsonParser().encodeResourceToString(responseBundle);
            System.out.println("Response JSON: " + bundleJson);

            return bundleJson;

        } catch (Exception e) {
            System.err.println("Error executing FHIR search:");
            e.printStackTrace(); // Print the stack trace for detailed debugging
            throw new RuntimeException("Error executing FHIR search", e); // Rethrow or handle the exception appropriately
        }
    }

    public String searchSlotsBySchedule(String schedule, String slotType, String start, Integer count) {
        try {
            Bundle responseBundle = client
                    .search()
                    .forResource(Slot.class)
                    .where(Slot.SCHEDULE.hasId(schedule))
                    .and(Slot.SLOT_TYPE.exactly().code(slotType))
                    .and(Slot.START.exactly().day(start))
                    .count(count)
                    .returnBundle(Bundle.class).accept("application/json")
                    .execute();
            if (responseBundle != null) {
                for (Bundle.Entry entry : responseBundle.getEntry()) {
                    IBaseResource resource = entry.getResource();
                    if (resource instanceof Slot) {
                        Slot slot = (Slot) resource;
                        if (slot.getStartElement() != null && !slot.getStartElement().isEmpty()) {
                        } else {
                            System.out.println("InstantDt is null or empty for Slot: " + slot.getId());
                        }
                    }
                }
            }

            String responseJson = fhirContext.newJsonParser().encodeResourceToString(responseBundle);
            System.out.println("responseJson: " + responseJson);

//            String json = objectMapper.writeValueAsString(responseBundle);
//            System.out.println("Response JSON: " + json);

            return responseJson;
        } catch (Exception e) {
            System.err.println("Error executing FHIR search:");
            e.printStackTrace(); // Print the stack trace for detailed debugging
            throw new RuntimeException("Error executing FHIR search", e); // Rethrow or handle the exception appropriately
        }
    }


    public List<String> fetchStartTime() throws IOException {
        String apiUrl = "https://azuhealow-preprod.healow.com/apps/api/v1/fhir/IFDECD/fhir/Slot?practice_code=IFDECD&schedule=b0f939e0-f422-40b7-bb66-93c9dc8ecbf9&slot-type=api1&slots_count=10&start=2024-06-24";
//        String date=2024-06-04;
//        String time;

        List<String> startTimes = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(new URL(apiUrl));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        for (JsonNode entry : root.path("entry")) {
            String startTime = entry.path("resource").path("start").asText();
            LocalDateTime dateTime = LocalDateTime.parse(startTime, formatter);
            String formattedDate = dateTime.toLocalDate().toString();
            String formattedTime = dateTime.toLocalTime().toString();

            if (formattedDate.equals(dateTime) && formattedTime.equals(startTime)) {
                startTimes.add(startTime);
            }

        }
        return startTimes;

    }

    //    public g
//    public String createSchedule(Schedule schedule) {
//        try {
//            // Callable<MethodOutcome> callable = () ->client
//            MethodOutcome outcome = client.create()
//                    .resource(schedule).accept("application/json+fhir")
//
//                    .execute();
//
//            // Print the outcome (optional)
//            if (outcome != null && outcome.getId() != null) {
//                System.out.println("Appointment created with ID: " + outcome.getId().getIdPart());
//            }
//            String responseJson = fhirContext.newJsonParser().encodeResourceToString((IBaseResource) outcome);
//            System.out.println("responseJson: " + responseJson);
//
//        } catch (Exception e) {
//            System.err.println("Error creating Appointment:");
//            e.printStackTrace();
//            throw new RuntimeException("Error creating Appointment", e);
//        }
//        return "hello";
//    }
    public String createSchedule(Schedule schedule) {
        try {
            MethodOutcome outcome = client.create()
                    .resource(schedule)
                    .prettyPrint()
                    .accept("application/json+fhir") // Ensure that JSON encoding is used
                    .execute();
            if (outcome != null && outcome.getId() != null) {
                System.out.println("Appointment created with ID: " + outcome.getId().getIdPart());
            }

            String responseJson = fhirContext.newJsonParser().encodeResourceToString((IBaseResource) outcome);
            System.out.println("responseJson: " + responseJson);

            return responseJson;

        } catch (Exception e) {
            System.err.println("Error creating Appointment:");
            e.printStackTrace();
            throw new RuntimeException("Error creating Appointment", e);
        }
    }

    public MethodOutcome updateSchedule(String scheduleId, Schedule schedule) {
        return client
                .update()
                .resource(schedule)
                .withId(scheduleId)
                .execute();
    }

    public String bookAppointment(Schedule schedule, String PracticeCode) throws IOException {
        this.clients = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // Increased connect timeout to 60 seconds
                .readTimeout(120, TimeUnit.SECONDS)   // Increased read timeout to 120 seconds
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://azuhealow-preprod.healow.com/apps/api/v1/fhir/IFDECD/dstu2/v2/Appointment").newBuilder();
        urlBuilder.addQueryParameter("Practice Code", PracticeCode);

        String bookurl = urlBuilder.build().toString();
        System.out.println("bookurl:::" + bookurl);
        String jsonBody = objectMapper.writeValueAsString(schedule);
        System.out.println("Request Body: " + jsonBody);

        Request request = new Request.Builder()
                .url(bookurl)
                .post(okhttp3.RequestBody.create(String.valueOf(schedule), MediaType.get("application/json+fhir"))).addHeader("Authorization", "Bearer AA1.W4QVB2DN3QsEwi03tTVtYLYxUtmWQY2SC1y2wAhGMlQzLbJZ9085fRxOX-1R6V9nX2sOMl7DmH_eN-neYuxJSx-m2Q66D-MJvfF4vn2vPlxmT30lZjS8rePHDAhRc8VfmwBTSF8et_EvV6Bwyq7Jw4rlrkJf5LBZR2nZYNKS_HzgnmhAIzzNm3kJ86rKriNE")
                .addHeader("Cookie", "JSESSIONID=78C5A71D81FA9FF3F943D799C9923F0F; ApplicationGatewayAffinity=cef1f429a0207a8fd583686fb86a5ca3; ApplicationGatewayAffinityCORS=cef1f429a0207a8fd583686fb86a5ca3; SERVERID=app02_8003")
                .build();

        System.out.println("request:::" + request);

        try (Response response = clients.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println("response:::" + response);

            return response.body().string();
        } catch (SocketTimeoutException e) {
            // Retry logic: retry the request after a short delay
            System.err.println("SocketTimeoutException occurred, retrying...");
            try {
                Thread.sleep(5000); // Wait for 2 seconds before retrying
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting to retry", interruptedException);
            }
            System.err.println("IOException while executing FHIR request:");
            e.printStackTrace(); // Print the stack trace for detailed debugging
            throw e;
        }
    }
}
