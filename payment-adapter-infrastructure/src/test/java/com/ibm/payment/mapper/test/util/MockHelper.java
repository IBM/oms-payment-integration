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
package com.ibm.payment.mapper.test.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import java.io.File;

import com.ibm.model.adyen.AdyenResponse;
import lombok.SneakyThrows;

public class MockHelper<T, V> {

  static ObjectMapper objectMapper = new ObjectMapper();
  static TestPair requestResponsePair = new TestPair<PaymentCollectionInput, PaymentCollectionOutput>();

  @SneakyThrows
  public static TestPair<PaymentCollectionInput, PaymentCollectionOutput> getPaymentsInterfacePair()
      {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    PaymentCollectionInput input = objectMapper
        .readValue(new File("src/test/resources/PaymentCollectionInput_Payment_interface.json"),
            PaymentCollectionInput.class);
    requestResponsePair.setInput(input);
    requestResponsePair.setOutput(null);
    return requestResponsePair;
  }

  @SneakyThrows
  public static AdyenResponse getAdyenResponseForPaymentsFlow() {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper
        .readValue(new File("src/test/resources/AdyenResp_payments_flow.json"),
            AdyenResponse.class);

  }
}
