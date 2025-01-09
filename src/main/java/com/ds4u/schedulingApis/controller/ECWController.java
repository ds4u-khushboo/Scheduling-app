package com.ds4u.schedulingApis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ECWController {

    private OkHttpClient client = new OkHttpClient.Builder().build();

    String PracticeCode;

    String schedule;
    ObjectMapper objectMapper = new ObjectMapper();

//    @RequestMapping(value = "/ecwSchedule", method = RequestMethod.GET)
//    public String getSchedule() throws IOException {

//        this.client = new OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(120, TimeUnit.SECONDS)
//                .build();
//
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://staging-fhir.ecwcloud.com/fhir/r4/FFBJCD").newBuilder();
//        //urlBuilder.addQueryParameter("Practice Code", PracticeCode);
//
//        String bookurl = urlBuilder.build().toString();
//        System.out.println("bookurl:::" + bookurl);
//        String jsonBody = objectMapper.writeValueAsString(schedule);
//        System.out.println("Request Body: " + jsonBody);
//
//        String url1 = UriComponentsBuilder.fromHttpUrl(apiConfig.getBaseUrl())
//                .queryParam("schedule", apiConfig.getSchedule(specialty, providerIndex))
//                .queryParam("slot-type", "api1")
//                .queryParam("start", "2024-07-18")
//                .queryParam("_count", "10")
//                .toUriString();
//
//        Request request = new Request.Builder()
//                .url(bookurl)
//                .post(okhttp3.RequestBody.create(String.valueOf(schedule), MediaType.get("application/json+fhir"))).
//                addHeader("Authorization", "Bearer eyJ4NXQjUzI1NiI6IlU1SlF6LVlLcnR1MHc2QTRpRTdzd3Y0N1ZnUHFZRnR1N0RIRzNVYW9aaXMiLCJraWQiOiJqd3QiLCJhbGciOiJSUzI1NiJ9.eyJwYXRpZW50R3VpZCI6IiIsInN1YiI6ImM1YjExODExLTQ1YjgtMTFlZC1hYTM1LTAwNTA1Njg0Y2UxYl9zYW0iLCJ3b3JrZmxvdyI6InN0YW5kYWxvbmVfcHJvdmlkZXIiLCJwcm92aWRlckd1aWQiOiJMdDJJRlI1QWg3Nm40ZDhURlA1Z0JBZnJ3cXhpZXNnODNjZWp6dFBrT0VJIiwiaXNzIjoiaHR0cHM6Ly9zdGFnaW5nLW9hdXRoc2VydmVyLmVjd2Nsb3VkLmNvbS9vYXV0aCIsInZlbmRvcklkIjoiMTI4OSIsImF1ZCI6WyJKdmZVWG12SGhjY3lDYmJMZHY5b2NGMHlnejhqOTdRTzhMSjdab3B5czFRIiwiaHR0cHM6Ly9zdGFnaW5nLWZoaXIuZWN3Y2xvdWQuY29tL2ZoaXIvcjQvRkZCSkNEIl0sIm5iZiI6MTcyMDYyNTY5NiwicHJhY3RpY2VHdWlkIjoiYzViMTE4MTEtNDViOC0xMWVkLWFhMzUtMDA1MDU2ODRjZTFiIiwic2NvcGUiOlsicGF0aWVudC9NZWRpY2F0aW9uLnJlYWQiLCJwYXRpZW50L1ByYWN0aXRpb25lci5yZWFkIiwibGF1bmNoL3BhdGllbnQiLCJ1c2VyL1BhdGllbnQucmVhZCIsInBhdGllbnQvTWVkaWNhdGlvblJlcXVlc3QucmVhZCIsInVzZXIvT2JzZXJ2YXRpb24ucmVhZCIsInBhdGllbnQvRGlhZ25vc3RpY1JlcG9ydC5yZWFkIiwidXNlci9DYXJlUGxhbi5yZWFkIiwicGF0aWVudC9PYnNlcnZhdGlvbi5yZWFkIiwicGF0aWVudC9DYXJlUGxhbi5yZWFkIiwicGF0aWVudC9FbmNvdW50ZXIucmVhZCIsInVzZXIvQ29uZGl0aW9uLnJlYWQiXSwiZXhwIjoxNzIwNjI1OTk2LCJpYXQiOjE3MjA2MjU2OTZ9.FOZGAsbmjOF3XYyJk7Rsi8oVwtyGoDOuIpOcKqEWWrBOVVbY2qcQ6_pAepfFFBIvSz7Zo-NzClUjv8Cww1uyFNzTL83uHKpr5tzYoBsGfgkUOOnI1S5CASMi7o-2tz3zpyDBkeGVHH_hDrR758IvdyDRuYTukhoVBOHbKO7cE3sSuGJHFcXAtngxLFV06oJ46KvVykpTNK58TyG602Q04GamtJrEKgZqYotWv9RqidEDLP2lnPqZgNG0-NpeTRGGHBZzqYkn2gbis4Bwu4vxyaSEZrUkSlVjilN--cNj0KKacUWtGEu-61o03ojJ4qGo0yhv4JIMmjsWLx3yoFHtejaWIIgQ_Ptzyqx_vFhuR8bNcuK2scrSlQMs2fzvKKj9t92lvwd3hqfqdmEooUYSENUrWbs26Otn5NwoB0nZvLLVZaWjAm-1lDz4VdoNFIUuCQEn0VUhdmxE6p8D0z4HHCAFWpS4TAycoLqQg8AA4ZSF_UXJIUfShVn9_h8r7SV5wMArNrYBp5Xg5XnpsaQgH257tVPQF6rqRC4LTE1hk3avZbSt1D8pq3UniGUlDCuBrcU0U_uOqM0BUw8mjnOOwkE8g994oZodY72VsIewGQy0K-FEYtuZQWzHLhXYhixdbQk4axOqvfROnHlEGq1wPLNDLoV_Lk7HMbbqCRFbWgoXWt6PCi0x8Aq-hGBb1WHjmIQxipU-iae4AQNBVpyH9oJZlwMfX7X_tNrcZzFkFIRfraL2pJwislQjpbEn7_JQv5vEmex2lwuR3vLAzM8thgPCohs6CxiEERqAmYUR9kZ_rYaVo7k05S_a2QOhoXKrsw")
//                .addHeader("Cookie", "JSESSIONID=78C5A71D81FA9FF3F943D799C9923F0F; ApplicationGatewayAffinity=cef1f429a0207a8fd583686fb86a5ca3; ApplicationGatewayAffinityCORS=cef1f429a0207a8fd583686fb86a5ca3; SERVERID=app02_8003")
//                .build();
//
//        System.out.println("request:::" + request);
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("Unexpected code " + response);
//            }
//            System.out.println("response:::" + response);
//
//            return response.body().string();
//        } catch (SocketTimeoutException e) {
//            System.err.println("SocketTimeoutException occurred, retrying...");
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException interruptedException) {
//                Thread.currentThread().interrupt();
//                throw new IOException("Interrupted while waiting to retry", interruptedException);
//            }
//            System.err.println("IOException while executing FHIR request:");
//            e.printStackTrace();
//            throw e;
//        }
}

