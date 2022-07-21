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
package com.ibm.adapter.service.implementation;

import com.ibm.adapter.util.ServiceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.adapter.context.PaymentInfraContext;
import com.ibm.adapter.context.RequestContext;
import com.ibm.adapter.payment.enums.ServiceType;
import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.adapter.service.interfaces.IReverseEngine;
import com.ibm.payment.infra.exception.PaymentConnectionException;

/**
 * 
 * ReverseService implements from {@link IReverseEngine} interface, used for
 * cancelling the authorised payment. Also, this invokes the adapter during the
 * full order cancellation scenarios. <br>
 * <strong>This method should be used if and only if the order is not charged.
 * In case of the charged order from OMS, then performRefund could be used to
 * process the captured payment.</strong>
 * 
 */
public class ReverseService implements IReverseEngine {

    private static final Logger logger = LogManager.getLogger(ReverseService.class);

    private PaymentInfraContext paymentInfraContext;

    /**
     * Constructor which injects respective payment service's infra context based on
     * the service type. PaymentInfraContext contains the infrastructure services
     * for the various service types and holds the respective context object from
     * the adapter.
     * 
     * @param paymentInfraContext {@link PaymentInfraContext} object contains
     *                            respective payment infra context for
     *                            ReverseService.
     */
    public ReverseService(PaymentInfraContext paymentInfraContext) {
	this.paymentInfraContext = paymentInfraContext;
    }

    /**
     * Method used to call the implementation of the Payment Gateway for processing
     * cancel payment API. Also, this invokes the adapter during the full order
     * cancellation scenarios. This method should be used if and only if the order
     * is not charged. In case of the charged order from OMS, then performRefund
     * could be used to process the captured payment.
     * 
     * for example, An order is created for the total of $10 and is cancelled.
     * 
     * @param requestContext         holds {@link RequestContext} object containing
     *                               the request's headers and path parameters.
     * @param paymentCollectionInput holds {@link PaymentCollectionInput} adapter's
     *                               input object is used to validate the
     *                               transaction type based on the service type.
     * @return The {@link PaymentCollectionOutput} adapter's output object is used
     *         for OMS mapping.
     */
    @SuppressWarnings("unchecked")
    @Override
    public PaymentCollectionOutput execute(RequestContext requestContext,
	    PaymentCollectionInput paymentCollectionInput) {
	logger.debug("ReverseService - execute()");
	PaymentCollectionOutput paymentCollectionOutput = new PaymentCollectionOutput();
	try {
	    ServiceUtils.setRequestPathParams(requestContext, paymentCollectionInput);
	    paymentCollectionOutput = paymentInfraContext.getReverseInfraService().performReverseAuth(
		    paymentCollectionInput, requestContext.getProperties(), getServiceType(),
		    requestContext.getRequestPathParams());
	} catch (PaymentConnectionException e) {
	    ServiceUtils.handlePaymentConnectionException(paymentCollectionOutput, e);
	} catch (Exception e) {
	    ServiceUtils.handleGenericException(e);
	}
	return paymentCollectionOutput;
    }

    /**
     * Method used to check eligibility for processing cancel payment API.
     * 
     * @param requestContext         holds {@link RequestContext} object containing
     *                               the request's headers and path parameters.
     * @param paymentCollectionInput holds {@link PaymentCollectionInput} adapter's
     *                               input object is used to validate the
     *                               transaction type based on the service type.
     * @return the boolean value which is eligible for cancel payment API
     */
    @Override
    public boolean isEligible(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
	logger.debug("ReverseService - isEligible()");
	if ((paymentCollectionInput.getTransactionType().contentEquals(ServiceType.REVERSE.toString())
		|| !requestContext.isSkipReverseCall())) {
	    return true;
	}
	return false;
    }
}
