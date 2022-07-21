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
import com.ibm.payment.infra.interfaces.ICaptureInfraService;
import com.ibm.payment.infra.interfaces.ICaptureResponseHandler;
import com.ibm.payment.infra.mapper.PaymentToAdyenRequestMapper;
import com.ibm.payment.infra.response.RestHttpClientResponse;
import com.ibm.payment.infra.responseHandlers.AdyenResponseHandler;
import com.ibm.payment.infra.util.InfraServiceUtils;
import com.ibm.payment.infra.util.RestHttpClientUtils;

import lombok.SneakyThrows;

/**
 * CaptureInfraService class implements from {@link ICaptureInfraService}
 * interface, used for Capturing the authorized payment and settlement happens
 * to merchants.The charge happens in two ways, Full order and Partial order
 * charges.
 * 
 */
public class CaptureInfraService<I> implements ICaptureInfraService<I> {

	private static final Logger logger = LogManager.getLogger(CaptureInfraService.class);

	private IRestHttpClient<AdyenRequest, AdyenRequestPathParam> iRestHttpClient;
	private ICaptureResponseHandler<AdyenResponse, ?> captureResponseHandler;

	@SuppressWarnings("unchecked")
	public CaptureInfraService() {
		captureResponseHandler = new AdyenResponseHandler();
		this.iRestHttpClient = RestHttpClientUtils.getRestHttpClient();
	}

	/**
	 * Performs payment capture operation asynchronously eg. submits the request to
	 * Adyen and the response is returned in the Webhook. Converts adapter input
	 * object like PaymentCollectionInput to payment service provider specific
	 * request object like AdyenRequest and invokes rest http client methods to get
	 * the response and converts response back to adapter's output object like
	 * PaymentCollectionOutput.
	 * 
	 * Method used to call the Implementation of the Payment Gateway for processing
	 * capture payment API. Also invoked by an adapter to charge and settle the
	 * amount against the payment. The charge happens in two ways :
	 * <ul>
	 * <li>Full order charge - If charge amount is equal to authorised amount .E.g :
	 * Order total is $10 and authorised amount is also $10. If the implementor
	 * wants to charge for the entire $10, this method should be invoked.</li>
	 * <li>Partial charge - If charge amount is less than authorised amount. for
	 * example, the order total is $10, and the authorised amount is $10. Oms wants
	 * to charge $5 now and needs to charge the remaining amount later.</li>
	 * </ul>
	 * 
	 * Handles logic for forward and reverse mapping, Adyen service invocation and
	 * exception handling.
	 * 
	 * @param paymentCollectionInput Holds {@link PaymentCollectionInput} payment
	 *                               input object, used for converting the input
	 *                               object to payment providers input for capture
	 *                               payment.
	 * @param config                 holds map value containing the request's
	 *                               headers and path parameters.
	 * @param apiName                Holds API names like payment processing,
	 *                               capture, refund, reverse and amend auth. Enum
	 *                               CAPTURE is the input for processing the capture
	 *                               in the payment adapters.
	 * @param requestPathParams      Holds payment provider's request path
	 *                               parameters used for capture payment processing.
	 * @return The {@link PaymentCollectionOutput} object,response from Adapter's
	 *         output object.
	 */
	@SneakyThrows
	@Override
	public PaymentCollectionOutput performCapture(PaymentCollectionInput paymentCollectionInput,
			Map<String, String> config, String apiName, I requestPathParams) {
		logger.debug("CaptureInfraService - performCapture");
		AdyenRequest adyenRequest = PaymentToAdyenRequestMapper.MAPPER.paymentToAdyenCaptureReq(paymentCollectionInput);
		RestHttpClientResponse restHttpClientResponse = this.iRestHttpClient.sendPost(apiName, config,
				(AdyenRequest) adyenRequest, (AdyenRequestPathParam) requestPathParams);
		AdyenResponse adyenResponse = InfraServiceUtils.getResponse(restHttpClientResponse);
		return (PaymentCollectionOutput) captureResponseHandler.applyCaptureResponse(adyenResponse);
	}

}
