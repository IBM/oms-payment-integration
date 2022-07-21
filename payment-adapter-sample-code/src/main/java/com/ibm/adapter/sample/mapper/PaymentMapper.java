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
package com.ibm.adapter.sample.mapper;

import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.adapter.sample.model.ExternalPaymentInput;
import com.ibm.adapter.sample.model.ExternalPaymentoutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper MAPPER = Mappers.getMapper(PaymentMapper.class);
    @Mapping(source = "authorizationId", target = "authorizationId")
    @Mapping(constant = "IBMAccount480ECOM", target = "merchantId")
    @Mapping(source = "authorizationId", target = "paymentReference")
    @Mapping(source = "paymentReference6", target = "paymentReference2")
    @Mapping(source = "paymentReference7", target = "paymentReference3")
    @Mapping(source = "orderNo", target = "orderNo")
    @Mapping(ignore = true, target = "paymentKey")
    @Mapping(source = "chargeType", target = "transactionType")
    @Mapping(source = "requestAmount", target = "requestAmount")
    @Mapping(source = "currentAuthorizationAmount", target = "authorizedAmount")
    PaymentCollectionInput omsToPayment(ExternalPaymentInput externalPaymentInput);


    @Mapping(source = "paymentReference", target = "PaymentReference5")
    @Mapping(constant = "true", target = "asynchRequestProcess")
    @Mapping(source = "asyncReqId", target = "asyncRequestIdentifier")
    ExternalPaymentoutput PaymentToOms(PaymentCollectionOutput paymentCollectionOutput);
}
