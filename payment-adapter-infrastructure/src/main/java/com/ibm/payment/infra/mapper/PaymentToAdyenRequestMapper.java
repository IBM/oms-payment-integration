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
package com.ibm.payment.infra.mapper;

import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.model.adyen.AdyenRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;

@Mapper
public interface PaymentToAdyenRequestMapper {
    PaymentToAdyenRequestMapper MAPPER = Mappers.getMapper(PaymentToAdyenRequestMapper.class);

    @Mapping(source = "paymentKey", target = "reference")
    @Mapping(source = "currency", target = "amount.currency")
    @Mapping(source = "requestAmount", target = "amount.value")
    @Mapping(source = "merchantId", target = "merchantAccount")
    @Mapping(target = "returnUrl", constant = "https://") // need to update
    @Mapping(source = "paymentType", target = "paymentMethod.type")
    @Mapping(source = "cardNumber", target = "paymentMethod.applePayToken")
    AdyenRequest paymentToAdyenAuthReq(PaymentCollectionInput paymentCollectionInput);

    // Capture
    @Mapping(source = "paymentKey", target = "reference")
    @Mapping(source = "currency", target = "amount.currency")
    @Mapping(target = "amount.value", expression = "java( paymentCollectionInput.getRequestAmount().abs().multiply(java.math.BigDecimal.valueOf(100)) )")
    @Mapping(source = "merchantId", target = "merchantAccount")
    AdyenRequest paymentToAdyenCaptureReq(PaymentCollectionInput paymentCollectionInput);

    // amount Update
    @Mapping(source = "paymentKey", target = "reference")
    @Mapping(source = "currency", target = "amount.currency")
    @Mapping(target = "amount.value", expression = "java( (paymentCollectionInput.getRequestAmount().abs().add(paymentCollectionInput.getAuthorizedAmount().abs()).multiply(java.math.BigDecimal.valueOf(100)) ))")
    @Mapping(source = "merchantId", target = "merchantAccount")
    AdyenRequest paymentToAdyenAmountUpdatesReq(PaymentCollectionInput paymentCollectionInput);

    // Full Reverse
    @Mapping(source = "paymentKey", target = "reference")
    @Mapping(source = "merchantId", target = "merchantAccount")
    AdyenRequest paymentToAdyenFullReverseReq(PaymentCollectionInput paymentCollectionInput);

    @Mapping(source = "paymentKey", target = "reference")
    @Mapping(source = "currency", target = "amount.currency")
    @Mapping(target = "amount.value", expression = "java(  paymentCollectionInput.getRequestAmount().abs().multiply(java.math.BigDecimal.valueOf(100)) )")
    @Mapping(source = "merchantId", target = "merchantAccount")
    AdyenRequest paymentToAdyenPartialRefundReq(PaymentCollectionInput paymentCollectionInput);

    @Mapping(target = "amount.value", expression = "java( paymentCollectionInput.getRequestAmount().abs().multiply(java.math.BigDecimal.valueOf(100)) )")
    @Mapping(source = "currency", target = "amount.currency")
    @Mapping(constant = "scheme", target = "paymentMethod.type")
    @Mapping(source = "paymentReference3", target = "paymentMethod.storedPaymentMethodId")
    @Mapping(source = "paymentKey", target = "reference")
    @Mapping(constant = "CONT_AUTH", target = "shopperInteraction")
    @Mapping(constant = "CARD_ON_FILE", target = "recurringProcessingModel")
    @Mapping(source = "merchantId", target = "merchantAccount")
    @Mapping(source = "paymentReference2", target = "shopperReference")
    @Mapping(ignore = true, target = "returnUrl")
    AdyenRequest paymentToAdyenPayments(PaymentCollectionInput paymentCollectionInput);
}
