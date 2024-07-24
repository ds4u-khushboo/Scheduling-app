package com.ds4u.schedulingApis.authservice;

import com.ds4u.schedulingApis.configuration.ApiDevConfig;
import com.ds4u.schedulingApis.entity.BookingInfo;
import com.ds4u.schedulingApis.respository.BookingInfoRespository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class BookService {

    private ApiDevConfig apiDevConfig;

    OkHttpClient client;

    @Autowired
    private BookingInfoRespository bookingInfoRespository;

//    public Appointment callHealoApi(BookingInfo request) throws IOException {
//        // Prepare the FHIR Appointment request
//        Appointment fhirAppointment = prepareAppointmentRequest(request);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonRequest = objectMapper.writeValueAsString(fhirAppointment);
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost httpPost = new HttpPost(apiUrl);
//            httpPost.setEntity(new StringEntity(jsonRequest));
//            httpPost.setHeader("Content-Type", "application/json+fhir");
//
//            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
//                int statusCode = response.getStatusLine().getStatusCode();
//
//                if (statusCode >= 400) {
//                    throw new IOException("HTTP error code: " + statusCode);
//                }
//
//                String jsonResponse = EntityUtils.toString(response.getEntity());
//
//                if (jsonResponse.isEmpty()) {
//                    throw new IOException("Empty response from server");
//                }
//
//                Appointment operationOutcomeResponse = objectMapper.readValue(jsonResponse, Appointment.class);
//
//                saveAppointmentEntity(request, jsonResponse);
//
//                return operationOutcomeResponse;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    private Appointment prepareAppointmentRequest(BookingInfo request) {
//        Appointment appointment = new Appointment();
//        appointment.setResourceType("Appointment");
//        appointment.setStatus("proposed");
//        appointment.setReason(new Appointment.Reason(request.getInfoForProvider()));
//        appointment.setDescription("The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list.");
//        appointment.setComment("Additional comments about the appointment.");
//
//        // Add Patient and Coverage details to 'contained'
//        Appointment.Contained patient = new Appointment.Contained();
//        patient.setResourceType("Patient");
//        patient.setId("patA");
////        patient.setName(List.of(new Appointment.Name("usual", List.of(request.getLastName()), List.of(request.getFirstName()))));
//        patient.setTelecom(List.of(
//                new Appointment.Telecom("phone", request.getPhoneNumber(), "mobile"),
//                new Appointment.Telecom("email", request.getEmail(), "home")
//        ));
//        patient.setGender(request.getSex());
//        patient.setBirthDate(request.getBirthDate());
//
//        Appointment.Contained coverage = new Appointment.Contained();
//        coverage.setResourceType("Coverage");
//        coverage.setType(new Appointment.Type(List.of(new Appointment.Coding("eCW_insurance_coding_system(Cash/Insurance/Not-Applicable)", "insurance_code", "insurance name")), "insurance name"));
//
//        appointment.setContained(List.of(patient, coverage));
//
//        // Add Slot
//        Appointment.Slot slot = new Appointment.Slot("a02365f3-8970-4c80-9056-84e6bda97fdf");
//        appointment.setSlot(List.of(slot));
//
//        // Add Participants
//        Appointment.Participant practitioner = new Appointment.Participant();
//        practitioner.setActor(new Appointment.Actor("Practitioner/1234564789"));
//        practitioner.setRequired("required");
//
//        Appointment.Participant patientParticipant = new Appointment.Participant();
//        patientParticipant.setActor(new Appointment.Actor("#patA"));
//        patientParticipant.setRequired("required");
//
//        appointment.setParticipant(List.of(practitioner, patientParticipant));
//
//        return appointment;
//    }
//
//    private void saveAppointmentEntity(BookingInfo request, String jsonResponse) {
//        BookingInfo entity = new BookingInfo();
//        entity.setFirstName(request.getFirstName());
//        entity.setLastName(request.getLastName());
//        entity.setBirthDate(request.getBirthDate());
//        entity.setSex(request.getSex());
//        entity.setPhoneNumber(request.getPhoneNumber());
//        entity.setEmail(request.getEmail());
//        entity.setInfoForProvider(request.getInfoForProvider());
//        entity.setDay(request.getDay());
//        entity.setTime(request.getTime());
//        bookingInfoRespository.save(entity);
//    }

    @GetMapping("/bookapi")
    public String bookAppointment(@RequestBody String schedule) throws IOException {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
ObjectMapper objectMapper=new ObjectMapper();
        // Parse incoming JSON to extract required fields
        JsonNode scheduleNode = objectMapper.readTree(schedule);

        // Extract fields for database storage
        String firstName = scheduleNode.at("/contained/0/name/0/given/0").asText();
        String lastName = scheduleNode.at("/contained/0/name/0/family/0").asText();
        String birthDate = scheduleNode.at("/contained/0/birthDate").asText();
        String sex = scheduleNode.at("/contained/0/gender").asText();
        String phoneNumber = scheduleNode.at("/contained/0/telecom/0/value").asText();
        String email = scheduleNode.at("/contained/0/telecom/1/value").asText();

        // Create BookingInfo object
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setFirstName(firstName);
        bookingInfo.setLastName(lastName);
        bookingInfo.setBirthDate(birthDate);
        bookingInfo.setSex(sex);
        bookingInfo.setPhoneNumber(phoneNumber);
        bookingInfo.setEmail(email);
        // Set other fields as needed for database storage

        // Save to database
        saveBookingInfo(bookingInfo);

        // Prepare JSON for third-party API
        // Assuming you need to construct a JSON body like {"bookingFor": "myself", ...}
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("bookingFor", "myself");
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        // Add other fields as needed

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // Build request to third-party API
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiDevConfig.getApiDevBookUrl()).newBuilder();
        String bookUrl = urlBuilder.build().toString();
        System.out.println("bookUrl::: " + bookUrl);

        Request request = new Request.Builder()
                .url(bookUrl)
                .post(okhttp3.RequestBody.create(jsonBody, MediaType.get("application/json")))
                .addHeader("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn())
                .build();

        // Execute request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println("response::: " + response);
            String jsonResponse = response.body().string();

            // Handle response from third-party API as needed

            return jsonResponse;
        } catch (IOException e) {
            System.err.println("IOException while executing request:");
            e.printStackTrace();
            throw e;
        }
    }

    private void saveBookingInfo(BookingInfo bookingInfo) {
        System.out.println("Saved booking info to database: " + bookingInfo);
        bookingInfoRespository.save(bookingInfo);
    }

}
