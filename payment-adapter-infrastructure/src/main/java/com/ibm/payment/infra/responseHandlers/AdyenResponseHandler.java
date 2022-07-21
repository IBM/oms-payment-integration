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
package com.ibm.payment.infra.responseHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.model.adyen.AdyenResponse;
import com.ibm.payment.infra.interfaces.IAmountUpdatesResponseHandler;
import com.ibm.payment.infra.interfaces.IAuthResponseHandler;
import com.ibm.payment.infra.interfaces.IReverseResponseHandler;
import com.ibm.payment.infra.interfaces.ICaptureResponseHandler;
import com.ibm.payment.infra.interfaces.IProcessPaymentResponseHandler;
import com.ibm.payment.infra.interfaces.IRefundResponseHandler;
import com.ibm.payment.infra.mapper.AdyenToPaymentResponseMapper;

/**
 * This class implemented all the response handler interfaces and provided
 * implementation for respective abstract methods to handle reverse mapping from
 * Payment processor specific http response, typically AdyenResponse to
 * Adapter's output object, typically PaymentCollectionOutput
 * 
 * Overridden methods:-
 * 
 * *
 * <ul>
 * <li>applyAuthResponse - maps authorization related http response typically
 * AdyenResponse to Adapter's output object</li>
 * <li>applyReverseResponse- maps reverse related http response typically
 * AdyenResponseto Adapter's output object</li>
 * <li>applyAmountUpdatesResponse - maps amend auth related http response
 * typically AdyenResponseto Adapter's output object</li>
 * <li>applyCaptureResponse - maps capture related http response typically
 * AdyenResponseto Adapter's output object</li>
 * <li>applyRefundResponse - maps refund related http response typically
 * AdyenResponseto Adapter's output object</li>
 * <li>applyPaymentsResponse - maps payment auth related http response typically
 * AdyenResponseto Adapter's output object</li>
 * 
 * </ul>
 * 
 */
public class AdyenResponseHandler implements IAuthResponseHandler<AdyenResponse, PaymentCollectionOutput>,
	IAmountUpdatesResponseHandler<AdyenResponse, PaymentCollectionOutput>,
	IReverseResponseHandler<AdyenResponse, PaymentCollectionOutput>,
	ICaptureResponseHandler<AdyenResponse, PaymentCollectionOutput>,
	IRefundResponseHandler<AdyenResponse, PaymentCollectionOutput>,
	IProcessPaymentResponseHandler<AdyenResponse, PaymentCollectionOutput> {

    private static final Logger logger = LogManager.getLogger(AdyenResponseHandler.class);

    /**
     * This method will be invoked by infra services to convert response to
     * PaymentCollectionOutput. Method used interface AdyenToPaymentResponseMapper
     * which is created using mapping library - mapstuct to do the reverse mapping.
     * 
     * Handles logic for reverse mapping of authorization
     * 
     * @param adyenResponse holds {@link AdyenResponse} objects contains
     *                      authorization response obtained from rest HttpClient
     * @return The {@link PaymentCollectionOutput} - Adapter's output object
     */
    @Override
    public PaymentCollectionOutput applyAuthResponse(AdyenResponse adyenResponse) {
	logger.debug("AdyenResponseHandler - applyAuthResponse");
	return AdyenToPaymentResponseMapper.MAPPER.adyenToPaymentAuthResponse(adyenResponse);
    }

    /**
     * This method will be invoked by infra services to convert response to
     * PaymentCollectionOutput.Method used interface AdyenToPaymentResponseMapper
     * which is created using mapping library - mapstuct to do the reverse mapping.
     * 
     * Handles logic for reverse mapping of full reversal scenarios.
     * 
     * @param adyenResponse holds {@link AdyenResponse} objects contains reverse
     *                      response obtained from rest HttpClient
     * @return The {@link PaymentCollectionOutput} - Adapter's output object
     */
    @Override
    public PaymentCollectionOutput applyReverseResponse(AdyenResponse adyenResponse) {
	logger.debug("AdyenResponseHandler - applyReverseResponse");
	return AdyenToPaymentResponseMapper.MAPPER.adyenToPaymentFullReverseResponse(adyenResponse);
    }

    /**
     * This method will be invoked by infra services to convert response to
     * PaymentCollectionOutput.Method used interface AdyenToPaymentResponseMapper
     * which is created using mapping library - mapstuct to do the reverse mapping.
     * 
     * Handles logic for reverse mapping of amend authorization
     * 
     * @param adyenResponse holds {@link AdyenResponse} objects contains amendauth
     *                      response obtained from rest HttpClient
     * @return The {@link PaymentCollectionOutput} - Adapter's output object
     */
    @Override
    public PaymentCollectionOutput applyAmountUpdatesResponse(AdyenResponse adyenResponse) {
	logger.debug("AdyenResponseHandler - applyAmountUpdatesResponse");
	return AdyenToPaymentResponseMapper.MAPPER.adyenToPaymentAmendAuthorizationResponse(adyenResponse);
    }

    /**
     * This method will be invoked by infra services to convert response to
     * PaymentCollectionOutput.Method used interface AdyenToPaymentResponseMapper
     * which is created using mapping library - mapstuct to do the reverse mapping.
     * 
     * Handles logic for reverse mapping of any payment capture
     * 
     * @param adyenResponse holds {@link AdyenResponse} objects contains reverse
     *                      response obtained from rest HttpClient
     * @return The {@link PaymentCollectionOutput} - Adapter's output object
     */
    @Override
    public PaymentCollectionOutput applyCaptureResponse(AdyenResponse adyenResponse) {
	logger.debug("AdyenResponseHandler - applyCaptureResponse");
	return AdyenToPaymentResponseMapper.MAPPER.adyenToPaymentCaptureResponse(adyenResponse);
    }

    /**
     * This method will be invoked by infra services to convert response to
     * PaymentCollectionOutput.Method used interface AdyenToPaymentResponseMapper
     * which is created using mapping library - mapstuct to do the reverse mapping.
     * 
     * Handles logic for reverse mapping of refund
     * 
     * @param adyenResponse holds {@link AdyenResponse} objects contains refund
     *                      response obtained from rest HttpClient
     * @return The {@link PaymentCollectionOutput} - Adapter's output object
     */
    @Override
    public PaymentCollectionOutput applyRefundResponse(AdyenResponse adyenResponse) {
	logger.debug("AdyenResponseHandler - applyRefundResponse");
	return AdyenToPaymentResponseMapper.MAPPER.adyenToPaymentPartialRefundResponse(adyenResponse);
    }

    /**
     * This method will be invoked by infra services to convert response to
     * PaymentCollectionOutput.Method used interface AdyenToPaymentResponseMapper
     * which is created using mapping library - mapstuct to do the reverse mapping.
     * 
     * Handles logic for reverse mapping of payment authorization
     * 
     * @param adyenResponse holds {@link AdyenResponse} objects contains auth
     *                      payment processing response obtained from rest
     *                      HttpClient
     * @return The {@link PaymentCollectionOutput} - Adapter's output object
     */
    @Override
    public PaymentCollectionOutput applyPaymentsResponse(AdyenResponse adyenResponse) {
	logger.debug("AdyenResponseHandler - applyPaymentsResponse");
	return AdyenToPaymentResponseMapper.MAPPER.adyenToPaymentsResponse(adyenResponse);
    }
}
