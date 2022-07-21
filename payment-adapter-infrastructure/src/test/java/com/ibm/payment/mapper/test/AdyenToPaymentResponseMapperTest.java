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
package com.ibm.payment.mapper.test;

import java.io.IOException;

import com.ibm.model.adyen.AdyenResponse;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.payment.infra.mapper.AdyenToPaymentResponseMapper;

import lombok.SneakyThrows;

public class AdyenToPaymentResponseMapperTest {

    @Test
    @SneakyThrows
    public void adyenToPaymentCaptureResponseTest() throws StreamReadException, DatabindException, IOException {

	// Given
	AdyenResponse response = new ObjectMapper()
		.readValue(getClass().getResourceAsStream("/adyenCaptureResponse.json"), AdyenResponse.class);

	// When
	PaymentCollectionOutput paymentCollectionOutput = AdyenToPaymentResponseMapper.MAPPER
		.adyenToPaymentCaptureResponse(response);
	// Then
	Assert.assertEquals(paymentCollectionOutput.getAsyncReqId(), response.getPspReference());
	Assert.assertEquals(paymentCollectionOutput.getPaymentReference(), response.getPaymentPspReference());
	Assert.assertEquals(paymentCollectionOutput.getAuthorizationId(), response.getPaymentPspReference());
	Assert.assertEquals(paymentCollectionOutput.getCaptureReference(), response.getPspReference());
	Assert.assertEquals(Boolean.TRUE, paymentCollectionOutput.isAsyncReq());

    }

    @Test
    @SneakyThrows
    public void adyenToPaymentAmountUpdateResponseTest() throws StreamReadException, DatabindException, IOException {
	// Given
	AdyenResponse response = new ObjectMapper()
		.readValue(getClass().getResourceAsStream("/adyenAmountUpdateResponse.json"), AdyenResponse.class);

	// When
	PaymentCollectionOutput paymentCollectionOutput = AdyenToPaymentResponseMapper.MAPPER
		.adyenToPaymentAmendAuthorizationResponse(response);
	// Then
	Assert.assertEquals(paymentCollectionOutput.getAsyncReqId(), response.getPspReference());
	Assert.assertEquals(paymentCollectionOutput.getPaymentReference(), response.getPaymentPspReference());
	Assert.assertEquals(paymentCollectionOutput.getAuthorizationId(), response.getPaymentPspReference());
	Assert.assertEquals(Boolean.TRUE, paymentCollectionOutput.isAsyncReq());
	Assert.assertEquals(0, paymentCollectionOutput.getAuthorizedAmount().doubleValue(), 0);

    }

    @Test
    @SneakyThrows
    public void adyenToPaymentFullReverseResponseTest() throws StreamReadException, DatabindException, IOException {
	// Given
	AdyenResponse response = new ObjectMapper()
		.readValue(getClass().getResourceAsStream("/adyenFullReverseResponse.json"), AdyenResponse.class);

	// When
	PaymentCollectionOutput paymentCollectionOutput = AdyenToPaymentResponseMapper.MAPPER
		.adyenToPaymentFullReverseResponse(response);
	// Then
	Assert.assertEquals(paymentCollectionOutput.getAsyncReqId(), response.getPspReference());
	Assert.assertEquals(paymentCollectionOutput.getPaymentReference(), response.getPaymentPspReference());
	Assert.assertEquals(paymentCollectionOutput.getReverseReference(), response.getPspReference());
	Assert.assertEquals(paymentCollectionOutput.getAuthorizationId(), response.getPaymentPspReference());
	Assert.assertEquals(Boolean.TRUE, paymentCollectionOutput.isAsyncReq());
	Assert.assertEquals(0, paymentCollectionOutput.getAuthorizedAmount().doubleValue(), 0);

    }

    @Test
    @SneakyThrows
    public void adyenToPaymentPartialRefundResponseTest() throws StreamReadException, DatabindException, IOException {
	AdyenResponse response = new ObjectMapper()
		.readValue(getClass().getResourceAsStream("/adyenPartialRefundResponse.json"), AdyenResponse.class);

	// When
	PaymentCollectionOutput paymentCollectionOutput = AdyenToPaymentResponseMapper.MAPPER
		.adyenToPaymentPartialRefundResponse(response);
	// Then
	Assert.assertEquals(paymentCollectionOutput.getAsyncReqId(), response.getPspReference());
	Assert.assertEquals(paymentCollectionOutput.getPaymentReference(), response.getPaymentPspReference());
	Assert.assertEquals(Boolean.TRUE, paymentCollectionOutput.isAsyncReq());

    }

}
