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
package com.ibm.payment.infra.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ibm.model.adyen.AdyenRequest;
import com.ibm.model.adyen.Amount;
import com.ibm.payment.infra.context.AdyenRequestPathParam;
import com.ibm.payment.infra.response.RestHttpClientResponse;
import com.ibm.payment.infra.util.RestHttpClientUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.adapter.payment.enums.ServiceType;

import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class StandardHttpClientTest {
    public MockWebServer mockWebServer;
    public IRestHttpClient<AdyenRequest, AdyenRequestPathParam> restHttpClient;
    public ObjectMapper objectMapper = new ObjectMapper();

    public static String pspReference;

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Before
    public void init() {
	mockWebServer = new MockWebServer();
	mockWebServer.start(8085);
	restHttpClient = RestHttpClientUtils.getRestHttpClient();
    }

    @Test
    public void test01PaymentAuthPositive() throws Exception {
	pspReference = "D84R7TVTWTTFWR82";
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();

	int expectedResponseCode = 200;
	String expectedResponseMessage = "{\"pspReference\":" + pspReference + ",\"resultCode\":\"Authorised\","
		+ "\"amount\":{\"currency\":\"USD\",\"value\":1000},\"merchantReference\":\"00001\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(200).setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.PROCESS_PAYMENTS.toString(),
		config, adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test02PaymentCapturePositive() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 200;
	String expectedResponseMessage = "{\"merchantAccount\":\"IBMTest533ECOM\",\"paymentPspReference\":"
		+ pspReference + ",\"pspReference\":\"JP3W2WDJMGXXGN82\","
		+ "\"reference\":\"00001\",\"status\":\"received\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.CAPTURE.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test03PaymentAmendAuthPositive() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 200;
	String expectedResponseMessage = "{\"merchantAccount\":\"IBMTest533ECOM\",\"paymentPspReference\":"
		+ pspReference
		+ ",\"pspReference\":\"M74RK3Z6B5BV9D82\",\"reference\":\"00001\",\"status\":\"received\","
		+ "\"amount\":{\"currency\":\"USD\",\"value\":2000}}";

	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient
		.sendPost(ServiceType.AMEND_AUTHORIZATION.toString(), config, adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test04PaymentRefundPositive() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 200;
	String expectedResponseMessage = "{\"merchantAccount\":\"IBMTest533ECOM\",\"paymentPspReference\":"
		+ pspReference
		+ ",\"pspReference\":\"RFVJP2SVWTTFWR82\",\"reference\":\"00001\",\"status\":\"received\","
		+ "\"amount\":{\"currency\":\"USD\",\"value\":1000}}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REFUND.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test05PaymentReversePositive() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 200;
	String expectedResponseMessage = "{\"merchantAccount\":\"IBMTest533ECOM\",\"paymentPspReference\":"
		+ pspReference + "," + "\"pspReference\":\"JP3W2WDJMGXXGN82\","
		+ "\"reference\":\"00001\",\"status\":\"received\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REVERSE.toString(), config,
		adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test06PaymentAuthNegative01() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();

	int expectedResponseCode = 400;
	String expectedResponseMessage = "{\"status\":400,\"errorCode\":\"702\",\"message\":\"Unexpected input: \\\", expected: }\",\"errorType\":\"validation\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.PROCESS_PAYMENTS.toString(),
		config, adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test07PaymentAuthNegative02() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();

	int expectedResponseCode = 401;
	String expectedResponseMessage = "{\"status\":401,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Unauthorized\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.PROCESS_PAYMENTS.toString(),
		config, adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test08PaymentAuthNegative03() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOMA");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();

	int expectedResponseCode = 403;
	String expectedResponseMessage = "{\"status\":403,\"errorCode\":\"901\",\"message\":\"Invalid Merchant Account\",\"errorType\":\"security\",\"pspReference\":\"CQQQP6Q8L5PFWR82\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.PROCESS_PAYMENTS.toString(),
		config, adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test09PaymentAuthNegative04() throws Exception {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();

	int expectedResponseCode = 503;
	String expectedResponseMessage = "{\"status\":503,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Server Error\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.url("https://localhosttest:8090");
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.PROCESS_PAYMENTS.toString(),
		config, adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test10PaymentCaptureNegative01() throws Exception {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 400;
	String expectedResponseMessage = "{\"status\":400,\"errorCode\":\"702\",\"message\":\"Unexpected input: \\\", expected: }\",\"errorType\":\"validation\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.CAPTURE.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test11PaymentCaptureNegative02() throws Exception {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 401;
	String expectedResponseMessage = "{\"status\":401,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Unauthorized\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.CAPTURE.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test12PaymentCaptureNegative03() throws Exception {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 403;
	String expectedResponseMessage = "{\"status\":403,\"errorCode\":\"901\",\"message\":\"Invalid Merchant Account\",\"errorType\":\"security\",\"pspReference\":\"CQQQP6Q8L5PFWR82\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.CAPTURE.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test13PaymentCaptureNegative04() throws Exception {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 503;
	String expectedResponseMessage = "{\"status\":503,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Server Error\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.url("https://localhosttest:8090");
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.CAPTURE.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test14PaymentAmendAuthNegative01() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 400;
	String expectedResponseMessage = "{\"status\":400,\"errorCode\":\"702\",\"message\":\"Unexpected input: \\\", expected: }\",\"errorType\":\"validation\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient
		.sendPost(ServiceType.AMEND_AUTHORIZATION.toString(), config, adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test15PaymentAmendAuthNegative02() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 401;
	String expectedResponseMessage = "{\"status\":401,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Unauthorized\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient
		.sendPost(ServiceType.AMEND_AUTHORIZATION.toString(), config, adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test16PaymentAmendAuthNegative03() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOMA");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 403;
	String expectedResponseMessage = "{\"status\":403,\"errorCode\":\"901\",\"message\":\"Invalid Merchant Account\",\"errorType\":\"security\",\"pspReference\":\"CQQQP6Q8L5PFWR82\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient
		.sendPost(ServiceType.AMEND_AUTHORIZATION.toString(), config, adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test17PaymentAmendAuthNegative04() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 503;
	String expectedResponseMessage = "{\"status\":503,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Server Error\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.url("https://localhosttest:8090");
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient
		.sendPost(ServiceType.AMEND_AUTHORIZATION.toString(), config, adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test18PaymentRefundNegative01() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 400;
	String expectedResponseMessage = "{\"status\":400,\"errorCode\":\"702\",\"message\":\"Unexpected input: \\\", expected: }\",\"errorType\":\"validation\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REFUND.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test19PaymentRefundNegative02() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 401;
	String expectedResponseMessage = "{\"status\":401,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Unauthorized\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REFUND.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test20PaymentRefundNegative03() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOMA");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 403;
	String expectedResponseMessage = "{\"status\":403,\"errorCode\":\"901\",\"message\":\"Invalid Merchant Account\",\"errorType\":\"security\",\"pspReference\":\"CQQQP6Q8L5PFWR82\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REFUND.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test21PaymentRefundNegative04() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOMA");
	adyenRequest.setReference("00001");
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.USD);
	amount.setValue(new BigDecimal(1000));
	adyenRequest.setAmount(amount);

	Map<String, String> config = new HashMap<>();

	AdyenRequestPathParam adyenRequestPathParam = new AdyenRequestPathParam();
	adyenRequestPathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 503;
	String expectedResponseMessage = "{\"status\":503,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Server Error\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.url("https://localhosttest:8090");
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REFUND.toString(), config,
		adyenRequest, adyenRequestPathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test22PaymentReverseNegative01() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 400;
	String expectedResponseMessage = "{\"status\":400,\"errorCode\":\"702\",\"message\":\"Unexpected input: \\\", expected: }\",\"errorType\":\"validation\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REVERSE.toString(), config,
		adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test23PaymentReverseNegative02() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOM");
	adyenRequest.setReference("00001");

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 401;
	String expectedResponseMessage = "{\"status\":401,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Unauthorized\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REVERSE.toString(), config,
		adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test24PaymentReverseNegative03() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOMA");
	adyenRequest.setReference("00001");

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 403;
	String expectedResponseMessage = "{\"status\":403,\"errorCode\":\"901\",\"message\":\"Invalid Merchant Account\",\"errorType\":\"security\",\"pspReference\":\"CQQQP6Q8L5PFWR82\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage);
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REVERSE.toString(), config,
		adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @Test
    public void test25PaymentReverseNegative04() {
	AdyenRequest adyenRequest = new AdyenRequest();
	adyenRequest.setMerchantAccount("IBMTest533ECOMA");
	adyenRequest.setReference("00001");

	Map<String, String> config = new HashMap<>();
	AdyenRequestPathParam pathParam = new AdyenRequestPathParam();
	pathParam.setPspPaymentReference(pspReference);

	int expectedResponseCode = 503;
	String expectedResponseMessage = "{\"status\":503,\"errorCode\":\"000\",\"message\":\"HTTP Status Response - Server Error\",\"errorType\":\"security\"}";
	MockResponse mockResponse = new MockResponse().setResponseCode(expectedResponseCode)
		.setBody(expectedResponseMessage).setHeader("X-Api-Key",
			"2AQElhmfuXNWTK0Qc+iSZsEkMovePHdgex1UtxSG6QG5Kz8M2Xymg+xDBXVsNvuR83LVYjEgiTGAH-zJz4+P5OZTZOw8/DFIlR5bGBPr6gnptpilg6P265OlE=-{GEk{M64>c@<<&r&");
	mockResponse.setBodyDelay(0, TimeUnit.SECONDS);
	mockResponse.setSocketPolicy(SocketPolicy.CONTINUE_ALWAYS);
	mockWebServer.url("https://localhosttest:8090");
	mockWebServer.enqueue(mockResponse);

	RestHttpClientResponse restHttpClientResponse = restHttpClient.sendPost(ServiceType.REVERSE.toString(), config,
		adyenRequest, pathParam);

	// assert
	assertNotNull(restHttpClientResponse);
	assertEquals(expectedResponseCode, restHttpClientResponse.getResponseCode());
	assertEquals(expectedResponseMessage, restHttpClientResponse.getResponse());
    }

    @SneakyThrows
    @After
    public void tearDown() {
	mockWebServer.shutdown();
    }
}
