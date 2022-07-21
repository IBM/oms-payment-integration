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

import com.ibm.model.adyen.AdyenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ibm.adapter.payment.model.PaymentCollectionOutput;

@Mapper
public interface AdyenToPaymentResponseMapper {

	AdyenToPaymentResponseMapper MAPPER = Mappers.getMapper(AdyenToPaymentResponseMapper.class);

	@Mapping(source = "pspReference", target = "asyncReqId")
	@Mapping(target = "authorizedAmount", constant = "0.0")
	@Mapping(target = "asyncReq", constant = "true")
	PaymentCollectionOutput adyenToPaymentAuthResponse(AdyenResponse adyenResponse);

	@Mapping(target = "asyncReq", constant = "true")
	@Mapping(source = "pspReference", target = "asyncReqId")
	@Mapping(source = "paymentPspReference", target = "paymentReference")
	@Mapping(source = "paymentPspReference", target = "authorizationId")
	@Mapping(target = "retry", constant = "false")
	@Mapping(source = "pspReference", target = "captureReference")
	PaymentCollectionOutput adyenToPaymentCaptureResponse(AdyenResponse adyenResponse);

	@Mapping(target = "asyncReq", constant = "true")
	@Mapping(source = "pspReference", target = "asyncReqId")
	@Mapping(source = "paymentPspReference", target = "paymentReference")
	@Mapping(source = "paymentPspReference", target = "authorizationId")
	@Mapping(target = "retry", constant = "false")
	@Mapping(target = "authorizedAmount", constant = "0.0")
	PaymentCollectionOutput adyenToPaymentAmendAuthorizationResponse(AdyenResponse adyenResponse);

	@Mapping(target = "asyncReq", constant = "true")
	@Mapping(source = "pspReference", target = "asyncReqId")
	@Mapping(source = "paymentPspReference", target = "paymentReference")
	@Mapping(source = "pspReference", target = "reverseReference")
	@Mapping(source = "paymentPspReference", target = "authorizationId")
	@Mapping(target = "retry", constant = "false")
	@Mapping(target = "authorizedAmount", constant = "0.0")
	PaymentCollectionOutput adyenToPaymentFullReverseResponse(AdyenResponse adyenResponse);

	@Mapping(target = "asyncReq", constant = "true")
	@Mapping(source = "pspReference", target = "asyncReqId")
	@Mapping(source = "paymentPspReference", target = "paymentReference")
	PaymentCollectionOutput adyenToPaymentPartialRefundResponse(AdyenResponse adyenResponse);

	@Mapping(target = "asyncReq", constant = "true")
	@Mapping(source = "pspReference", target = "asyncReqId")
	@Mapping(source = "paymentPspReference", target = "paymentReference")
	PaymentCollectionOutput adyenToPaymentsResponse(AdyenResponse adyenResponse);
}
