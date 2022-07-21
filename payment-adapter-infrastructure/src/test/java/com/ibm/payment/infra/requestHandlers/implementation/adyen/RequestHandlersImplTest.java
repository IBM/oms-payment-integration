/*
IBM Confidential
IBM Sterling OMS Payment Integration Adapter
(C) Copyright IBM Corp. 2022
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.ibm.payment.infra.requestHandlers.implementation.adyen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ibm.model.adyen.AdyenRequest;
import com.ibm.payment.infra.context.AdyenRequestPathParam;
import org.apache.http.client.config.RequestConfig;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import com.ibm.adapter.payment.enums.ServiceType;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class RequestHandlersImplTest {

    RequestHandlersImpl<AdyenRequest, AdyenRequestPathParam, RequestConfig> request = new RequestHandlersImpl<AdyenRequest, AdyenRequestPathParam, RequestConfig>();

    @Test
    public void test01ProcessPaymentGetResorcesURL() throws Exception {

	String expectedURL = "http://localhost:8085/v68/payments";

	String actualURL = request.getResourceURL(ServiceType.PROCESS_PAYMENTS.toString(), new AdyenRequestPathParam());

	assertEquals(expectedURL, actualURL);
    }

    @Test
    public void test02CapturePaymentGetResorcesURL() throws Exception {

	String pspReference = "D84R7TVTWTTFWR82";
	String expectedURL = "http://localhost:8085/v68/payments/" + pspReference + "/captures";

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	String actualURL = request.getResourceURL(ServiceType.CAPTURE.toString(), adyenRequestPathParam);

	assertEquals(expectedURL, actualURL);
    }

    @Test
    public void test03RefundPaymentGetResorcesURL() throws Exception {

	String pspReference = "D84R7TVTWTTFWR82";
	String expectedURL = "http://localhost:8085/v68/payments/" + pspReference + "/refunds";

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	String actualURL = request.getResourceURL(ServiceType.REFUND.toString(), adyenRequestPathParam);

	assertEquals(expectedURL, actualURL);
    }

    @Test
    public void test04ReversePaymentGetResorcesURL() throws Exception {

	String pspReference = "D84R7TVTWTTFWR82";
	String expectedURL = "http://localhost:8085/v68/payments/" + pspReference + "/cancels";

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	String actualURL = request.getResourceURL(ServiceType.REVERSE.toString(), adyenRequestPathParam);

	assertEquals(expectedURL, actualURL);
    }

    @Test
    public void test05AmountUpdatesPaymentGetResorcesURL() throws Exception {

	String pspReference = "D84R7TVTWTTFWR82";
	String expectedURL = "http://localhost:8085/v68/payments/" + pspReference + "/amountUpdates";

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	String actualURL = request.getResourceURL(ServiceType.AMEND_AUTHORIZATION.toString(), adyenRequestPathParam);

	assertEquals(expectedURL, actualURL);
    }
}
