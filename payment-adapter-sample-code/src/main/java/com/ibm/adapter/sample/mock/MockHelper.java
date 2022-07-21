package com.ibm.adapter.sample.mock;

import com.ibm.adapter.context.RequestContext;
import com.ibm.adapter.sample.model.ExternalPaymentInput;

public class MockHelper {
    public static ExternalPaymentInput getExternalPaymentInput() {
        ExternalPaymentInput input=new ExternalPaymentInput();
        input.setCurrency("USD");
        input.setPaymentKey("12345");
        input.setAuthorizationId("AUTH_ID_1");
        input.setChargeType("AUTHORIZATION");
        input.setPaymentReference5("AUTH_ID_1");
        input.setOrderNo("ORDR001");
        input.setCurrentAuthorizationAmount("10.0");
        input.setRequestAmount("-10.0");
        return input;
    }

   public static RequestContext getRequestContext() {
        RequestContext ctx=new RequestContext();
        ctx.setContextId("CTX_001");
        ctx.setSkipAuthCall(false);
        return ctx;
    }
}
