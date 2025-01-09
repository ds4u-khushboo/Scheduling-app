package com.ds4u.schedulingApis.controller;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import org.springframework.stereotype.Component;

@Component
public class AuthHeaderInterceptor implements IClientInterceptor {

    private final String authToken;

    public AuthHeaderInterceptor() {
        this.authToken = "Bearer AA1.W4QVB2DN3QsEwi03tTVtYLYxUtmWQY2SC1y2wAhGMlQzLbJZ9085fRxOX-1R6V9nX2sOMl7DmH_eN-neYuxJSx-m2Q66D-MJvfF4vn2vPlxmT30lZjS8rePHDAhRc8VfmwBTSF8et_EvV6Bwyq7Jw4rlrkJf5LBZR2nZYNKS_HzgnmhAIzzNm3kJ86rKriNE";
    }

    @Override
    public void interceptRequest(IHttpRequest theRequest) {
        theRequest.addHeader(Constants.HEADER_CONTENT_TYPE, "application/json+fhir");
        theRequest.addHeader(Constants.HEADER_CONTENT_TYPE, "application/json");
        theRequest.addHeader("Authorization", authToken);
        theRequest.addHeader("Access-Control-Allow-Origin", "*");
        theRequest.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        theRequest.addHeader("Cookie", "JSESSIONID=78C5A71D81FA9FF3F943D799C9923F0F; ApplicationGatewayAffinity=cef1f429a0207a8fd583686fb86a5ca3; ApplicationGatewayAffinityCORS=cef1f429a0207a8fd583686fb86a5ca3; SERVERID=app02_8003");
    }

    @Override
    public void interceptResponse(IHttpResponse theResponse) {
    }
}
