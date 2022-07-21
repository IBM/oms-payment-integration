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

import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.model.adyen.AdyenRequest;
import com.ibm.model.adyen.AdyenResponse;
import com.ibm.payment.infra.mapper.AdyenToPaymentResponseMapper;
import com.ibm.payment.infra.mapper.PaymentToAdyenRequestMapper;
import com.ibm.payment.mapper.test.util.MockHelper;
import com.ibm.payment.mapper.test.util.TestPair;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProcessPaymentsMapperTest {


  @SneakyThrows
  @Test
  @DisplayName("AdyenRequest mapper test for payment interface")
   void test_model_to_aden_mapping(){
    TestPair requestResponsePair= new MockHelper().getPaymentsInterfacePair();
    AdyenRequest request=PaymentToAdyenRequestMapper.MAPPER.paymentToAdyenPayments(
        (PaymentCollectionInput) requestResponsePair.getInput());
    PaymentCollectionInput paymentInput= (PaymentCollectionInput) requestResponsePair.getInput();
    Assertions.assertEquals(paymentInput.getOrderNo(),request.getReference());
    Assertions.assertEquals(paymentInput.getRequestAmount(),request.getAmount().getValue());
    Assertions.assertEquals(paymentInput.getCurrency(),request.getAmount().getCurrency().toString());
    Assertions.assertEquals(paymentInput.getMerchantId(),request.getMerchantAccount());
    Assertions.assertEquals("scheme",request.getPaymentMethod().getType());
    Assertions.assertEquals("ABCDEFGHIJK",request.getPaymentMethod().getStoredPaymentMethodId());
    Assertions.assertEquals("ContAuth",request.getShopperInteraction().toString());
    Assertions.assertEquals("CardOnFile",request.getRecurringProcessingModel().toString());
    Assertions.assertEquals(paymentInput.getPaymentReference2(),request.getShopperReference());
  }

  @SneakyThrows
  @Test
  @DisplayName("AdyenRequest mapper test for payment interface")
  void test_adyen_to_model_mapping(){
    AdyenResponse response=MockHelper.getAdyenResponseForPaymentsFlow();
    PaymentCollectionOutput actualOutput= AdyenToPaymentResponseMapper.MAPPER
        .adyenToPaymentsResponse(response);
    Assertions.assertEquals(response.getPspReference(),actualOutput.getAsyncReqId());
    Assertions.assertTrue(actualOutput.isAsyncReq());
  }
}
