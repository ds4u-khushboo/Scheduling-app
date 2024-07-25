package com.ds4u.schedulingApis.controller;

import com.ds4u.schedulingApis.authservice.AppointmentService;
import com.ds4u.schedulingApis.authservice.BookService;
import com.ds4u.schedulingApis.configuration.ApiDevConfig;
import com.ds4u.schedulingApis.dataModels.ProviderSlot;
import com.ds4u.schedulingApis.dataModels.Slot;
import com.ds4u.schedulingApis.entity.BookingInfo;
import com.ds4u.schedulingApis.entity.PatientInfo;
import com.ds4u.schedulingApis.entity.Provider;
import com.ds4u.schedulingApis.requestmodel.Patient;
import com.ds4u.schedulingApis.respository.BookingInfoRespository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private ApiDevConfig apiDevConfig;

    @Bean
    public ApiDevConfig apiConfig() {
        return new ApiDevConfig();
    }

    OkHttpClient client;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    BookService bookService;

    @Autowired
    BookingInfoRespository bookingInfoRespository;

    @RequestMapping(value = "/getSchedules", method = RequestMethod.GET)
    public Map<String, List<String>> getAvailableSlots(@RequestParam String provider, @RequestParam String date) {

        return appointmentService.getAvailableSlots(provider, date);
    }

    @RequestMapping(value = "/getSlots", method = RequestMethod.GET)
    public Map<String, List<Slot>> getAvailableSlots(@RequestParam String Provider, @RequestParam List<String> date) throws IOException {
        return appointmentService.findSlotsByProviderAndDates(Provider, date);
    }

    @RequestMapping(value = "/getSlotss", method = RequestMethod.GET)
    public ProviderSlot getAvailableSlotsbyweek(@RequestParam String date) throws IOException {
        return appointmentService.getSlotsForWeek(date);
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity<String> BookAppointment(@RequestBody Patient patient) {

        try {
            String result = appointmentService.bookAppointment(patient);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to book appointment: " + e.getMessage());
        }
    }

    @PostMapping("/bookapi")
    public String bookAppointment(@RequestBody String Appointment) throws IOException {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        // Parse incoming JSON to extract required fields
        JsonNode scheduleNode = objectMapper.readTree(Appointment);

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

        // Save to database
        bookingInfoRespository.save(bookingInfo);
        saveBookingInfo(bookingInfo);

        // Prepare JSON for third-party API
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("bookingFor", "myself");
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        // Add other fields as needed

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // Build request to third-party API
        String bookUrl = apiDevConfig.getApiDevBookUrl();
        System.out.println("bookUrl::: " + bookUrl);
        System.out.println("jsonBody::: " + jsonBody);

        Request request = new Request.Builder()
                .url(bookUrl).post(okhttp3.RequestBody.create(jsonBody, MediaType.get("application/json+fhir")))
//                .post(requestBody.put(jsonBody, String.valueOf(MediaType.get("application/json+fhir"))))
                .addHeader("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn())
                //.addHeader("Content-Type", "application/json+fhir")
                .build();

        // Print request details for debugging
        System.out.println("Request URL: " + request.url());
        System.out.println("Request Headers: " + request.headers());
        System.out.println("Request Body: " + jsonBody);

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

    @RequestMapping(value = "/providersList", method = RequestMethod.GET)
    public HashMap<String, List<String>> getProviders(@RequestParam String speciality) {
        return appointmentService.getProvidersFromFile(speciality);
    }

    @RequestMapping(value = "/providers", method = RequestMethod.GET)
    public List<Provider> getProvidersBySpeciality(@RequestParam @NotNull String speciality, @RequestParam(required = false) String providerName) {
        return appointmentService.getProvidersList(speciality, providerName);
    }

    private PatientInfo mapToPatientInfo(PatientInfo request) {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setFirstName(request.getFirstName());
        patientInfo.setLastName(request.getLastName());
        patientInfo.setAddressLine1(request.getAddressLine1());

        return patientInfo;
    }


    @RequestMapping(value = "/patientByProvider", method = RequestMethod.GET)
    public void getTimeSlotsByProvider(@RequestParam Long provider) {
        appointmentService.getPatientList(provider);
    }


}
