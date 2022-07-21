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
import com.ibm.payment.infra.interfaces.IProcessPaymentResponseHandler;
import com.ibm.payment.infra.interfaces.IProcessPaymentsInfraService;
import com.ibm.payment.infra.mapper.PaymentToAdyenRequestMapper;
import com.ibm.payment.infra.response.RestHttpClientResponse;
import com.ibm.payment.infra.responseHandlers.AdyenResponseHandler;
import com.ibm.payment.infra.util.InfraServiceUtils;
import com.ibm.payment.infra.util.RestHttpClientUtils;

import lombok.SneakyThrows;

/**
 * ProcessPaymentsInfraService class implements from
 * {@link IProcessPaymentsInfraService} interface, used for processing
 * authorisation payment API. Also, this happens in the following scenarios to
 * create the authorisation: <br>
 * 
 * <ul>
 * <li>Authorisation Expire - Existing authorisation has expired and new
 * authorisation has to be generated.</li>
 * <li>Order modification with an increase in order totals. OMS creates an
 * additional authorisation for the requested amount. <br>
 * Performs below :<br>
 * <ul>
 * <li>1. Map PaymentCollectionInput to AdyenRequest</li>
 * <li>2. Call Adyen payments service 3.Reverse mapping of AdyenResponse to
 * PaymentCollectionOutput</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @param <I> Holds Payment input objects
 */
public class ProcessPaymentsInfraService<I> implements IProcessPaymentsInfraService<I> {

    private static final Logger logger = LogManager.getLogger(ProcessPaymentsInfraService.class);

    private IRestHttpClient<AdyenRequest, AdyenRequestPathParam> iRestHttpClient;
    private IProcessPaymentResponseHandler<AdyenResponse, ?> paymentResponseHandler;

    @SuppressWarnings("unchecked")
    public ProcessPaymentsInfraService() {
	this.paymentResponseHandler = new AdyenResponseHandler();
	this.iRestHttpClient = RestHttpClientUtils.getRestHttpClient();
    }
    
    /**
     * Performs payment auth operation asynchronously eg. submits the request to
     * Adyen and the response is returned in the Webhook. Converts adapter input
     * object like PaymentCollectionInput to payment service provider specific
     * request object like AdyenRequest and invokes rest http client methods to get
     * the response and converts response back to adapter's output object like
     * PaymentCollectionOutput.
     * 
     * Method used to call the Implementation of the Payment Gateway for processing
     * the payment API for Authorization. Also this should be used in following
     * scenarios: <br>
     * <ul>
     * <li>Auth expiry - Existing authorisation has expired and new authorisation
     * has to be generated.</li>
     * <li>Order modification with increase in order totals - Order worth $200 was
     * created. Customer has added additional items to the order and the order total
     * is now $250.Oms creates an additional authorisation request for $50.</li>
     * </ul>
     * 
     * @param paymentCollectionInput Holds {@link PaymentCollectionInput} object
     *                               used to perform process payment.
     * @param config                 Holds the request header values from
     *                               RequestContext Properties Value.
     * @param apiName                Indicates the Apiname of the adapter.
     * @param requestPathParams      Holds the request path parameters.
     * @return The {@link PaymentCollectionOutput} object
     */
    @SneakyThrows
    @Override
    public PaymentCollectionOutput performPayment(PaymentCollectionInput paymentCollectionInput, Map config,
	    String apiName, Object requestPathParams) {
	logger.debug("ProcessPaymentsInfraService - performPayment");
	AdyenRequest adyenRequest = PaymentToAdyenRequestMapper.MAPPER.paymentToAdyenPayments(paymentCollectionInput);
	RestHttpClientResponse restHttpClientResponse = this.iRestHttpClient.sendPost(apiName, config,
		(AdyenRequest) adyenRequest, (AdyenRequestPathParam) requestPathParams);
	AdyenResponse adyenResponse = InfraServiceUtils.getResponse(restHttpClientResponse);
	return (PaymentCollectionOutput) this.paymentResponseHandler.applyPaymentsResponse(adyenResponse);
    }
}
