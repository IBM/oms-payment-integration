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
package com.ibm.payment.infra.implementation;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.model.adyen.AdyenRequest;
import com.ibm.model.adyen.AdyenResponse;
import com.ibm.payment.infra.client.IRestHttpClient;
import com.ibm.payment.infra.context.AdyenRequestPathParam;
import com.ibm.payment.infra.interfaces.IRefundInfraService;
import com.ibm.payment.infra.interfaces.IRefundResponseHandler;
import com.ibm.payment.infra.mapper.PaymentToAdyenRequestMapper;
import com.ibm.payment.infra.response.RestHttpClientResponse;
import com.ibm.payment.infra.responseHandlers.AdyenResponseHandler;
import com.ibm.payment.infra.util.InfraServiceUtils;
import com.ibm.payment.infra.util.RestHttpClientUtils;

import lombok.SneakyThrows;

/**
 * RefundInfraService class implements from {@link IRefundInfraService}
 * interface, which involved the process of Refunding the captured payment in
 * the scenario of returning the order items. This refund happens in two ways
 * <br>
 * <ul>
 * <li>Full refund - A order created with two items. The customer returns the
 * entire order.</li>
 * <li>Partial refund - A order created with two items, the customer wants to
 * return an item</li>
 * </ul>
 * 
 */
public class RefundInfraService<I> implements IRefundInfraService<I> {

	private static final Logger logger = LogManager.getLogger(RefundInfraService.class);

	private IRestHttpClient<AdyenRequest, AdyenRequestPathParam> iRestHttpClient;
	private IRefundResponseHandler<AdyenResponse, ?> refundResponseHandler;

	@SuppressWarnings("unchecked")
	public RefundInfraService() {
		this.refundResponseHandler = new AdyenResponseHandler();
		this.iRestHttpClient = RestHttpClientUtils.getRestHttpClient();
	}

	/**
	 * Performs refund operation asynchronously eg. submits the request to Adyen and
	 * the response is returned in the Webhook. Converts adapter input object like
	 * PaymentCollectionInput to payment service provider specific request object
	 * like AdyenRequest and invokes rest http client methods to get the response
	 * and converts response back to adapter's output object like
	 * PaymentCollectionOutput.Original PSP reference is required to be stamped in
	 * url to call adyen refund api.
	 *
	 * Method used to call the Implementation of the Payment Gateway for processing
	 * refund payment API. Also it is invoked by adapter for all the various refund
	 * scenarios. <br>
	 * <ul>
	 * <li>Full refund - Order was created for $100 . Customer returns the entire
	 * order.</li>
	 * <li>Partial refund - Order was created for $100 , customer wants to return an
	 * item worth $40</li>
	 * </ul>
	 * 
	 * Handles logic for forward and reverse mapping ,Adyen service invocation and
	 * exception handling.
	 * 
	 * @param paymentCollectionInput Holds {@link PaymentCollectionInput} payment
	 *                               input object, used for converting the input
	 *                               object to payment providers input for refund
	 *                               payment.
	 * @param config                 holds map value containing the request's
	 *                               headers and path parameters.
	 * @param apiName                Holds API names like payment processing,
	 *                               capture, refund, reverse and amend auth. Enum
	 *                               REFUND is the input for processing the refund
	 *                               in the payment adapters.
	 * @param requestPathParams      Holds payment provider's request path
	 *                               parameters used for processing refund payment .
	 * @return The {@link PaymentCollectionOutput} object,response from Adapter's
	 *         output object.
	 */
	@SneakyThrows
	@Override
	public PaymentCollectionOutput performRefund(PaymentCollectionInput paymentCollectionInput,
			Map<String, String> config, String apiName, I requestPathParams) {
		logger.debug("RefundInfraService - performRefund");
		AdyenRequest adyenRequest = PaymentToAdyenRequestMapper.MAPPER
				.paymentToAdyenPartialRefundReq(paymentCollectionInput);
		RestHttpClientResponse restHttpClientResponse = this.iRestHttpClient.sendPost(apiName, config,
				(AdyenRequest) adyenRequest, (AdyenRequestPathParam) requestPathParams);
		AdyenResponse adyenResponse = InfraServiceUtils.getResponse(restHttpClientResponse);
		return (PaymentCollectionOutput) refundResponseHandler.applyRefundResponse(adyenResponse);
	}
}
