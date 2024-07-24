package com.ds4u.schedulingApis.authservice;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class EhrApiProd {


    @Value("api.base.url")
    String apiBaseURL;

    public String callapi(String actor, String date, String type, String identifier, String location) throws UnsupportedEncodingException {

        // Replace with your API endpoint URL
        String apiUrl = "https://connect4.healow.com/apps/api/v1/fhir//dstu2/Schedule?";

        String accessToken = "AA1.8ch2YQVV-1hzCRUgBessWez2oUiqlQbtHTVPqlZSH3h02qFqfu-kIXl30KiPMlwjtJQnPHKYPxsxX9kU2-KdpNMIeQLpRLlK2fwUtiJlvYXCUk8E4LPk1j0q7-Wss2GpKvP_tmhX_5Xd2K0Lr8W78rlgjBuY0rHq81MeE0h4cYJJBX3M89_p_1ij8IOo3m_8ZYO79MXTPTER29FLlw98Rg";
        String encodedParam1 = URLEncoder.encode(actor, "UTF-8");
        String encodedParam2 = URLEncoder.encode(type, "UTF-8");
        String encodedParam3 = URLEncoder.encode(date, "UTF-8");
        String encodedParam4 = URLEncoder.encode(identifier, "UTF-8");
        String encodedParam5 = URLEncoder.encode(location, "UTF-8");

        String urlWithParams = apiUrl + "actor=" + encodedParam1 + "&type=" + encodedParam2 + "&date=" + encodedParam3 + "&identifier=" + encodedParam4 +"&actor.location=" + encodedParam5;

        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(urlWithParams);

            System.out.println("urlWithParams"+urlWithParams);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Response status code: " + statusCode);

            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            if (entity != null) {
                System.out.println("Response body:");
                System.out.println(responseBody);
            }
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}