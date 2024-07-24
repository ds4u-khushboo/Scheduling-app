package com.ds4u.schedulingApis.controller;

import com.ds4u.schedulingApis.respository.BookingInfoRespository;
import com.ds4u.schedulingApis.service.HealowDevService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SlotController {

    @Autowired
    private BookingInfoRespository bookingInfoRespository;

    @Autowired
    private HealowDevService healowDevService;

    @GetMapping("/apischedule")
    public String getSchedule(@RequestParam String actor, @RequestParam String date, @RequestParam String type, @RequestParam String identifier, @RequestParam Integer actorLocation) throws IOException {
        return healowDevService.getSchedule(actor, date, type, identifier, actorLocation);
    }

    @GetMapping("/apislot")
    public String getSlots(@RequestParam String schedule, String slotType, String start, Integer count) throws IOException {
        return healowDevService.getSlots(schedule, slotType, start, count);
    }

    @GetMapping("/apicombineschedule")
    public JsonNode getSchedule(@RequestParam String actor, String date, String identifier, String type, String location, String slotType, String start, int count) throws IOException {

            return healowDevService.getSchedules(actor, date, identifier, type, location, slotType, start, count);
        }
    }

//
//    @PostMapping("/bookapi")
//
//    public String bookAppointment() throws IOException {
//        String apiUrl = apiConfig.getApiDevBookUrl();
//        String jsonRequest = "{\n" +
//                "    \"resourceType\": \"Appointment\",\n" +
//                "    \"contained\": [\n" +
//                "        {\n" +
//                "            \"resourceType\": \"Patient\",\n" +
//                "            \"id\": \"patA\",\n" +
//                "            \"name\": [\n" +
//                "                {\n" +
//                "                    \"use\": \"usual\",\n" +
//                "                    \"family\": [\n" +
//                "                        \"testfamily\"\n" +
//                "                    ],\n" +
//                "                    \"given\": [\n" +
//                "                        \"testgiven\"\n" +
//                "                    ]\n" +
//                "                }\n" +
//                "            ],\n" +
//                "            \"telecom\": [\n" +
//                "                {\n" +
//                "                    \"system\": \"phone\",\n" +
//                "                    \"value\": \"0000000000\",\n" +
//                "                    \"use\": \"mobile\"\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"system\": \"email\",\n" +
//                "                    \"value\": \"test@example.com\",\n" +
//                "                    \"use\": \"home\"\n" +
//                "                }\n" +
//                "            ],\n" +
//                "            \"gender\": \"male\",\n" +
//                "            \"birthDate\": \"1970-01-01\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"resourceType\": \"Coverage\",\n" +
//                "            \"type\": {\n" +
//                "                \"coding\": [\n" +
//                "                    {\n" +
//                "                        \"system\": \"eCW_insurance_coding_system(Cash/Insurance/Not-Applicable)\",\n" +
//                "                        \"code\": \"insurance_code\",\n" +
//                "                        \"display\": \"insurance name\"\n" +
//                "                    }\n" +
//                "                ],\n" +
//                "                \"text\": \"insurance name\"\n" +
//                "            }\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"reason\": {\n" +
//                "        \"text\": \"The reason that this appointment is being scheduled. (e.g. Regular checkup)\"\n" +
//                "    },\n" +
//                "    \"description\": \"The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list.\",\n" +
//                "    \"slot\": [\n" +
//                "        {\n" +
//                "            \"reference\": \"a02365f3-8970-4c80-9056-84e6bda97fdf\"\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"comment\": \"Additional comments about the appointment.\",\n" +
//                "    \"participant\": [\n" +
//                "        {\n" +
//                "            \"actor\": {\n" +
//                "                \"reference\": \"Practitioner/1234564789\"\n" +
//                "            },\n" +
//                "            \"required\": \"required\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"actor\": {\n" +
//                "                \"reference\": \"#patA\"\n" +
//                "            },\n" +
//                "            \"required\": \"required\"\n" +
//                "        }\n" +
//                "    ]\n" +
//                "}";
//
//        //   String response = bookAppointment(apiUrl, jsonRequest);
//        //     System.out.println(response);
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(apiUrl);
//
//        // Set the headers
//        httpPost.setHeader("Content-Type", "application/json+fhir");
//        httpPost.setHeader("Authorization", "Bearer " + apiConfig.getDevBearerToekn());
//        // Set the request body
//        StringEntity entity = new StringEntity(jsonRequest);
//        httpPost.setEntity(entity);
//
//        // Execute the request
//        CloseableHttpResponse response = httpClient.execute(httpPost);
//
//        try {
//            if (response.getStatusLine().getStatusCode() == 307) {
//                String newUrl = response.getFirstHeader("Location").getValue();
//                return bookAppointment();
//            }
//
//            String responseBody = EntityUtils.toString(response.getEntity());
//
//            // Check the status code
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == 201) {
//                // Handle successful response
//                return responseBody;
//            } else {
//                // Handle error response
//                throw new RuntimeException("Failed with HTTP error code: " + statusCode);
//            }
//        } finally {
//            response.close();
//            httpClient.close();
//        }
//    }
//
//    private BookingInfo mapJsonToBookingInfo(String jsonResponse) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        return objectMapper.readValue(jsonResponse, BookingInfo.class);
//    }
//
//    private void saveBookingInfo(BookingInfo bookingInfo) {
//        System.out.println("Saved booking info to database: " + bookingInfo);
//        bookingInfoRespository.save(bookingInfo);
//    }
//}
//
//
