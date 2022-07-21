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
import com.ibm.payment.infra.interfaces.IReverseInfraService;
import com.ibm.payment.infra.interfaces.IReverseResponseHandler;
import com.ibm.payment.infra.mapper.PaymentToAdyenRequestMapper;
import com.ibm.payment.infra.response.RestHttpClientResponse;
import com.ibm.payment.infra.responseHandlers.AdyenResponseHandler;
import com.ibm.payment.infra.util.InfraServiceUtils;
import com.ibm.payment.infra.util.RestHttpClientUtils;

import lombok.SneakyThrows;

/**
 * ReverseInfraService implements from {@link IReverseInfraService} interface,
 * used for Reversing the authorised payment. Also, adapter invokes this during
 * the full order reversal scenarios.
 *
 */
public class ReverseInfraService<I> implements IReverseInfraService<I> {

    private static final Logger logger = LogManager.getLogger(ReverseInfraService.class);

    private IRestHttpClient<AdyenRequest, AdyenRequestPathParam> iRestHttpClient;
    private IReverseResponseHandler<AdyenResponse, ?> reverseResponseHandler;

    @SuppressWarnings("unchecked")
    public ReverseInfraService() {
	reverseResponseHandler = new AdyenResponseHandler();
	this.iRestHttpClient = RestHttpClientUtils.getRestHttpClient();
    }

    /**
     * 
     * Performs reverse authorization operation asynchronously eg. submits the
     * request to Adyen and the response is returned in the Webhook. Converts
     * adapter input object like PaymentCollectionInput to payment service provider
     * specific request object like AdyenRequest and invokes rest http client
     * methods to get the response and converts response back to adapter's output
     * object like PaymentCollectionOutput.
     * 
     * example scenarios would be : Method used to call the implementation of the
     * Payment Gateway for processing reverse payment API. Also, this invokes the
     * adapter during the full order reversal scenarios. This method should be used
     * if and only if the order is not charged. In case of the charged order from
     * OMS, then performRefund could be used to process the captured payment.
     * 
     * for example, An order is created for the total of $10 and is cancelled.
     * 
     * Handles logic for forward and reverse mapping ,Adyen service invocation and
     * exception handling.
     * 
     * <strong>This method should be used if and only if the order is not charged.
     * In case of the charged order from OMS, then performRefund could be used to
     * process the captured payment.</strong>
     * 
     * @param paymentCollectionInput Holds {@link PaymentCollectionInput} payment
     *                               input object, used for converting the input
     *                               object to payment providers input for reverse
     *                               payment.
     * @param config                 holds map value containing the request's
     *                               headers and path parameters.
     * @param apiName                Holds API names like payment processing,
     *                               capture, refund, reverse and amend auth. Enum
     *                               REVERSE is the input for processing the reverse
     *                               in the payment adapters.
     * @param requestPathParams      Holds payment provider's request path
     *                               parameters used for processing reverse payment
     *                               .
     * @return The {@link PaymentCollectionOutput} object,response from Adapter's
     *         output object.
     */
    @SneakyThrows
    @Override
    public PaymentCollectionOutput performReverseAuth(PaymentCollectionInput paymentCollectionInput,
	    Map<String, String> config, String apiName, I requestPathParams) {
	logger.debug("ReverseInfraService - performReverse");
	AdyenRequest adyenRequest = PaymentToAdyenRequestMapper.MAPPER
		.paymentToAdyenFullReverseReq(paymentCollectionInput);
	RestHttpClientResponse restHttpClientResponse = this.iRestHttpClient.sendPost(apiName, config,
		(AdyenRequest) adyenRequest, (AdyenRequestPathParam) requestPathParams);
	AdyenResponse adyenResponse = InfraServiceUtils.getResponse(restHttpClientResponse);
	return (PaymentCollectionOutput) reverseResponseHandler.applyReverseResponse(adyenResponse);
    }

}
