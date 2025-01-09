package com.ds4u.schedulingApis.controller;

import com.ds4u.schedulingApis.configuration.ApiDevConfig;
import com.ds4u.schedulingApis.configuration.ApiLiveConfig;
import com.ds4u.schedulingApis.entity.BookingInfo;
import com.ds4u.schedulingApis.respository.BookingInfoRespository;
import com.ds4u.schedulingApis.service.HealowDevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private BookingInfoRespository bookingInfoRespository;
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();

    }

    @Autowired
    private ApiDevConfig apiDevConfig;


//book apo for dev for full payload
//    @PostMapping("/book")
//    public void bookAppointment(@RequestBody String appointment) {
//        try {
//            URL url = new URL("https://azuhealow-preprod.healow.com/apps/api/v1/fhir/IFDECD/dstu2/v2/Appointment");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json+fhir");
//            conn.setRequestProperty("Authorization", "Bearer AA1.W4QVB2DN3QsEwi03tTVtYLYxUtmWQY2SC1y2wAhGMlQzLbJZ9085fRxOX-1R6V9nX2sOMl7DmH_eN-neYuxJSx-m2Q66D-MJvfF4vn2vPlxmT30lZjS8rePHDAhRc8VfmwBTSF8et_EvV6Bwyq7Jw4rlrkJf5LBZR2nZYNKS_HzgnmhAIzzNm3kJ86rKriNE");
//            //conn.setRequestProperty("Accept", "application/json+fhir");
//            conn.setDoOutput(true);
//
//            // Replace with your JSON appointment data
//            String jsonInputString = "{\n" +
//                    "  \"resourceType\": \"Appointment\",\n" +
//                    "  \"contained\": [\n" +
//                    "    {\n" +
//                    "      \"resourceType\": \"Patient\",\n" +
//                    "      \"id\": \"patA\",\n" +
//                    "      \"name\": [\n" +
//                    "        {\n" +
//                    "          \"use\": \"usual\",\n" +
//                    "          \"family\": [\n" +
//                    "            \"testfamily\"\n" +
//                    "          ],\n" +
//                    "          \"given\": [\n" +
//                    "            \"testgiven\"\n" +
//                    "          ],\n" +
//                    "          \"suffix\": [\n" +
//                    "            \"MSc\"\n" +
//                    "          ]\n" +
//                    "        }\n" +
//                    "      ],\n" +
//                    "      \"telecom\": [\n" +
//                    "        {\n" +
//                    "          \"system\": \"phone\",\n" +
//                    "          \"value\": \"0000000000\",\n" +
//                    "          \"use\": \"mobile\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "          \"system\": \"email\",\n" +
//                    "          \"value\": \"test@example.com\",\n" +
//                    "          \"use\": \"home\"\n" +
//                    "        }\n" +
//                    "      ],\n" +
//                    "      \"gender\": \"male\",\n" +
//                    "      \"birthDate\": \"1970-01-01\",\n" +
//                    "      \"maritalStatus\": {\n" +
//                    "        \"coding\": [\n" +
//                    "          {\n" +
//                    "            \"system\": \"http://hl7.org/fhir/v3/MaritalStatus\",\n" +
//                    "            \"code\": \"M\",\n" +
//                    "            \"display\": \"Married\"\n" +
//                    "          }\n" +
//                    "        ],\n" +
//                    "        \"text\": \"Getrouwd\"\n" +
//                    "      },\n" +
//                    "      \"multipleBirthBoolean\": true,\n" +
//                    "      \"contact\": [\n" +
//                    "        {\n" +
//                    "          \"relationship\": [\n" +
//                    "            {\n" +
//                    "              \"coding\": [\n" +
//                    "                {\n" +
//                    "                  \"system\": \"http://hl7.org/fhir/patient-contact-relationship\",\n" +
//                    "                  \"code\": \"partner\"\n" +
//                    "                }\n" +
//                    "              ]\n" +
//                    "            }\n" +
//                    "          ],\n" +
//                    "          \"name\": {\n" +
//                    "            \"use\": \"usual\",\n" +
//                    "            \"family\": [\n" +
//                    "              \"testfamilycontact\"\n" +
//                    "            ],\n" +
//                    "            \"given\": [\n" +
//                    "              \"testgivencontact\"\n" +
//                    "            ]\n" +
//                    "          },\n" +
//                    "          \"telecom\": [\n" +
//                    "            {\n" +
//                    "              \"system\": \"phone\",\n" +
//                    "              \"value\": \"0000000000\",\n" +
//                    "              \"use\": \"mobile\"\n" +
//                    "            }\n" +
//                    "          ]\n" +
//                    "        }\n" +
//                    "      ],\n" +
//                    "      \"address\": [\n" +
//                    "        {\n" +
//                    "          \"use\": \"home\",\n" +
//                    "          \"type\": \"both\",\n" +
//                    "          \"text\": \"132, My Street, Kingston, NY, 12401, US\",\n" +
//                    "          \"line\": [\n" +
//                    "            \"132\",\n" +
//                    "            \"My Street\"\n" +
//                    "          ],\n" +
//                    "          \"city\": \"Kingston\",\n" +
//                    "          \"state\": \"NY\",\n" +
//                    "          \"postalCode\": \"12401\",\n" +
//                    "          \"country\": \"US\"\n" +
//                    "        }\n" +
//                    "      ],\n" +
//                    "      \"communication\": [\n" +
//                    "        {\n" +
//                    "          \"language\": {\n" +
//                    "            \"coding\": [\n" +
//                    "              {\n" +
//                    "                \"system\": \"urn:ietf:bcp:47\",\n" +
//                    "                \"code\": \"nl\",\n" +
//                    "                \"_code\": {\n" +
//                    "                  \"fhir_comments\": [\n" +
//                    "                    \"    IETF language tag    \"\n" +
//                    "                  ]\n" +
//                    "                },\n" +
//                    "                \"display\": \"Dutch\"\n" +
//                    "              }\n" +
//                    "            ],\n" +
//                    "            \"text\": \"Nederlands\"\n" +
//                    "          },\n" +
//                    "          \"preferred\": true\n" +
//                    "        }\n" +
//                    "      ],\n" +
//                    "      \"extension\": [\n" +
//                    "        {\n" +
//                    "          \"url\": \"http://hl7.org/fhir/us/core/StructureDefinition/us-core-race\",\n" +
//                    "          \"extension\": [\n" +
//                    "            {\n" +
//                    "              \"url\": \"ombCategory\",\n" +
//                    "              \"valueCoding\": {\n" +
//                    "                \"system\": \"urn:oid:2.16.840.1.113883.6.238\",\n" +
//                    "                \"code\": \"1002-5\",\n" +
//                    "                \"display\": \"American Indian or Alaska Native\"\n" +
//                    "              }\n" +
//                    "            },\n" +
//                    "            {\n" +
//                    "              \"url\": \"detailed\",\n" +
//                    "              \"valueCoding\": {\n" +
//                    "                \"system\": \"urn:oid:2.16.840.1.113883.6.238\",\n" +
//                    "                \"code\": \"1442-3\",\n" +
//                    "                \"display\": \"Indian Township\"\n" +
//                    "              }\n" +
//                    "            },\n" +
//                    "            {\n" +
//                    "              \"url\": \"text\",\n" +
//                    "              \"valueString\": \"American Indian or Alaska Native\"\n" +
//                    "            }\n" +
//                    "          ]\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "          \"url\": \"http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity\",\n" +
//                    "          \"extension\": [\n" +
//                    "            {\n" +
//                    "              \"url\": \"detailed\",\n" +
//                    "              \"valueCoding\": {\n" +
//                    "                \"system\": \"urn:oid:2.16.840.1.113883.6.238\",\n" +
//                    "                \"code\": \"2155-0\",\n" +
//                    "                \"display\": \"Central American\"\n" +
//                    "              }\n" +
//                    "            },\n" +
//                    "            {\n" +
//                    "              \"url\": \"ombCategory\",\n" +
//                    "              \"valueCoding\": {\n" +
//                    "                \"system\": \"urn:oid:2.16.840.1.113883.6.238\",\n" +
//                    "                \"code\": \"2135-2\",\n" +
//                    "                \"display\": \"Hispanic or Latino\"\n" +
//                    "              }\n" +
//                    "            },\n" +
//                    "            {\n" +
//                    "              \"url\": \"text\",\n" +
//                    "              \"valueString\": \"Hispanic or Latino\"\n" +
//                    "            }\n" +
//                    "          ]\n" +
//                    "        }\n" +
//                    "      ],\n" +
//                    "      \"managingOrganization\": {\n" +
//                    "        \"reference\": \"Organization/f001\",\n" +
//                    "        \"display\": \"Burgers University Medical Centre\"\n" +
//                    "      }\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "      \"resourceType\": \"Coverage\",\n" +
//                    "      \"type\": {\n" +
//                    "        \"coding\": [\n" +
//                    "          {\n" +
//                    "            \"system\": \"eCW_insurance_coding_system(Cash/Insurance/Not-Applicable)\",\n" +
//                    "            \"code\": \"insurance_code\",\n" +
//                    "            \"display\": \"insurance name\"\n" +
//                    "          }\n" +
//                    "        ],\n" +
//                    "        \"text\": \"insurance name\"\n" +
//                    "      }\n" +
//                    "    }\n" +
//                    "  ],\n" +
//                    "  \"id\": \"\",\n" +
//                    "  \"status\": \"proposed\",\n" +
//                    "  \"reason\": {\n" +
//                    "    \"text\": \"The reason that this appointment is being scheduled. (e.g. Regular checkup)\"\n" +
//                    "  },\n" +
//                    "  \"description\": \"The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list.\",\n" +
//                    "  \"slot\": [\n" +
//                    "    {\n" +
//                    "      \"reference\": \"a02365f3-8970-4c80-9056-84e6bda97fdf\"\n" +
//                    "    }\n" +
//                    "  ],\n" +
//                    "  \"comment\": \"Additional comments about the appointment.\",\n" +
//                    "  \"participant\": [\n" +
//                    "    {\n" +
//                    "      \"actor\": {\n" +
//                    "        \"reference\": \"Practitioner/1234564789\"\n" +
//                    "      },\n" +
//                    "      \"required\": \"required\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "      \"actor\": {\n" +
//                    "        \"reference\": \"#patA\"\n" +
//                    "      },\n" +
//                    "      \"required\": \"required\"\n" +
//                    "    }\n" +
//                    "  ]\n" +
//                    "}";
//
//            try (OutputStream os = conn.getOutputStream()) {
//                byte[] input = jsonInputString.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("HTTP Response Code: " + responseCode);
//
//            conn.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //book api for modified payload for dev
    @PostMapping("/dev/bookAppointment")
    public String bookAppointment(@RequestBody BookingInfo bookingInfo) {
        try {
            String baseUrl = apiDevConfig.getApiDevBookUrl();
            URL url = new URL(baseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json+fhir");
            conn.setRequestProperty("Authorization", "Bearer " + apiDevConfig.getDevBearerToekn());
            conn.setDoOutput(true);

            // Construct JSON payload
            String jsonInputString = "{\n" +
                    "    \"resourceType\": \"Appointment\",\n" +
                    "    \"contained\": [\n" +
                    "        {\n" +
                    "            \"resourceType\": \"Patient\",\n" +
                    "             \"id\": \"patA\",\n"+
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
                    "    \"id\": \"\",\n" +
                    "    \"status\": \"proposed\",\n" +
                    "    \"reason\": {\n" +
                    "        \"text\": \"Regular checkup\"\n" +
                    "    },\n" +
                    "    \"description\": \"Appointment description\",\n" +
                    "    \"slot\": [\n" +
                    "        {\n" +
                    "            \"reference\": "+bookingInfo.getSlotId() +
                    "        }\n" +
                    "    ],\n" +
                    "    \"comment\": \"Additional comments\",\n" +
                    "    \"participant\": [\n" +
                    "        {\n" +
                    "            \"actor\": {\n" +
                    "                \"reference\": "+apiDevConfig.getActorDevNpi() +
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

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
//            System.out.println("HTTP Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                bookingInfoRespository.save(bookingInfo);
                return "BookingInfo saved to database";
            } else {
                // Print error response
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return responseCode +response.toString();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

//    @PostMapping("/book/appointment")
//    public String booking(@RequestBody BookingInfo bookingInfo) throws IOException {
//
//        return healowDevService.book(bookingInfo);
//    }
}
