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

import com.ibm.model.adyen.AdyenRequest;
import com.ibm.model.adyen.Amount;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.payment.infra.mapper.PaymentToAdyenRequestMapper;
import com.ibm.payment.mapper.test.util.MockHelper;
import com.ibm.payment.mapper.test.util.TestPair;

import lombok.SneakyThrows;

import java.math.BigDecimal;

public class PaymentToAdyenRequestMapperTest {

    PaymentCollectionInput paymentCollectionInput;

    @BeforeEach
    public void initialize() {
	TestPair requestResponsePair = MockHelper.getPaymentsInterfacePair();
	paymentCollectionInput = (PaymentCollectionInput) requestResponsePair.getInput();
    }

    @Test
    @SneakyThrows
    public void paymentToAdyenCaptureReqTest() {

	// Given

	AdyenRequest modelreq = new AdyenRequest();
	modelreq.setReference(paymentCollectionInput.getOrderNo());
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.fromValue(paymentCollectionInput.getCurrency()));
	amount.setValue(paymentCollectionInput.getRequestAmount().multiply(new BigDecimal(100)));
	modelreq.setAmount(amount);
	modelreq.setMerchantAccount(paymentCollectionInput.getMerchantId());

	// When
	AdyenRequest req = PaymentToAdyenRequestMapper.MAPPER.paymentToAdyenCaptureReq(paymentCollectionInput);
	// Then

	Assert.assertEquals(req, modelreq);

    }

    @Test
    @SneakyThrows
    public void paymentToAdyenAmountUpdatesReq() {

	// Given

	AdyenRequest modelreq = new AdyenRequest();
	modelreq.setReference(paymentCollectionInput.getOrderNo());
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.fromValue(paymentCollectionInput.getCurrency()));
	amount.setValue(paymentCollectionInput.getRequestAmount().add(paymentCollectionInput.getAuthorizedAmount()));
	modelreq.setAmount(amount);
	modelreq.setMerchantAccount(paymentCollectionInput.getMerchantId());

	// When
	AdyenRequest req = PaymentToAdyenRequestMapper.MAPPER.paymentToAdyenAmountUpdatesReq(paymentCollectionInput);

	// Then
	Assert.assertEquals(req, modelreq);

    }

    @Test
    @SneakyThrows
    public void paymentToAdyenFullReverseReq() {

	// Given

	AdyenRequest modelreq = new AdyenRequest();
	modelreq.setReference(paymentCollectionInput.getOrderNo());
	modelreq.setMerchantAccount(paymentCollectionInput.getMerchantId());

	// When
	AdyenRequest req = PaymentToAdyenRequestMapper.MAPPER.paymentToAdyenFullReverseReq(paymentCollectionInput);
	// Then

	Assert.assertEquals(req, modelreq);

    }

    @Test
    @SneakyThrows
    public void paymentToAdyenPartialRefundReq() {

	// Given

	AdyenRequest modelreq = new AdyenRequest();
	modelreq.setReference(paymentCollectionInput.getOrderNo());
	Amount amount = new Amount();
	amount.setCurrency(Amount.Currency.fromValue(paymentCollectionInput.getCurrency()));
	amount.setValue(paymentCollectionInput.getRequestAmount().multiply(new BigDecimal(100)));
	modelreq.setAmount(amount);
	modelreq.setMerchantAccount(paymentCollectionInput.getMerchantId());

	// When
	AdyenRequest req = PaymentToAdyenRequestMapper.MAPPER.paymentToAdyenPartialRefundReq(paymentCollectionInput);
	// Then

	Assert.assertEquals(req, modelreq);

    }

}
