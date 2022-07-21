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
import com.ibm.payment.infra.interfaces.IAmendAuthInfraService;
import com.ibm.payment.infra.interfaces.IAmountUpdatesResponseHandler;
import com.ibm.payment.infra.mapper.PaymentToAdyenRequestMapper;
import com.ibm.payment.infra.response.RestHttpClientResponse;
import com.ibm.payment.infra.responseHandlers.AdyenResponseHandler;
import com.ibm.payment.infra.util.InfraServiceUtils;
import com.ibm.payment.infra.util.RestHttpClientUtils;

import lombok.SneakyThrows;

/**
 * AmendAuthInfraService implements from {@link IAmendAuthInfraService}
 * interface, used for processing the Amend Auth payment APIs used for
 * increasing or decreasing the authorized amount. Whenever there is a
 * modification in the order, we need to increase or decrease the authorized
 * amount if Adyen allows it.
 *
 */
public class AmendAuthInfraService<I> implements IAmendAuthInfraService<I> {

    private static final Logger logger = LogManager.getLogger(AmendAuthInfraService.class);

    private IRestHttpClient<AdyenRequest, AdyenRequestPathParam> iRestHttpClient;
    private IAmountUpdatesResponseHandler<AdyenResponse, ?> amountUpdatesResponseHandler;

    @SuppressWarnings("unchecked")
    public AmendAuthInfraService() {
	this.amountUpdatesResponseHandler = new AdyenResponseHandler();
	this.iRestHttpClient = RestHttpClientUtils.getRestHttpClient();
    }

    /**
     * Performs amend authorization operation asynchronously eg. submits the request
     * to Adyen and the response is returned in the Webhook. Converts adapter input
     * object like PaymentCollectionInput to payment service provider specific
     * request object like AdyenRequest and invokes rest http client methods to get
     * the response and converts response back to adapter's output object like
     * PaymentCollectionOutput. Also handles logic for forward and reverse mapping,
     * Adyen service invocation, validation and exception handling. example
     * scenarios would be :
     * <ul>
     * <li>Partial order cancellation - Order worth $100 was created . Customer
     * cancels items worth $50. In this case, OMS might choose to cancel the
     * remaining $50.</li>
     * <li>Customer appeasement - Order worth $100 was created. The retailer has
     * provided appeasement worth $20. In this case, the retailer does not want to
     * block the entire $100 from the customer's account. So OMS will reverse $20
     * worth of authorisation.</li>
     * </ul>
     * 
     * 
     * @param paymentCollectionInput Holds {@link PaymentCollectionInput} payment
     *                               input object, used for converting the input
     *                               object to payment providers input for amend
     *                               auth payment.
     * @param config                 holds map value containing the request's
     *                               headers and path parameters.
     * @param apiName                Holds API names like payment processing,
     *                               capture, refund, reverse and amend auth. Enum
     *                               AMEND_AUTH is the input for processing the
     *                               amend authorization in the payment adapters.
     * @param requestPathParams      Holds payment provider's request path
     *                               parameters used for amend auth payment
     *                               processing.
     * @return The {@link PaymentCollectionOutput} object,response from Adapter's
     *         output object.
     */
    @SneakyThrows
    @Override
    public PaymentCollectionOutput performAmendAuth(PaymentCollectionInput paymentCollectionInput,
	    Map<String, String> config, String apiName, I requestPathParams) {
	logger.debug("AmendAuthInfraService - performAmendAuth");
	AdyenRequest adyenRequest = PaymentToAdyenRequestMapper.MAPPER
		.paymentToAdyenAmountUpdatesReq(paymentCollectionInput);
	RestHttpClientResponse restHttpClientResponse = this.iRestHttpClient.sendPost(apiName, config, adyenRequest,
		(AdyenRequestPathParam) requestPathParams);
	AdyenResponse adyenResponse = InfraServiceUtils.getResponse(restHttpClientResponse);
	return (PaymentCollectionOutput) amountUpdatesResponseHandler.applyAmountUpdatesResponse(adyenResponse);
    }
}
