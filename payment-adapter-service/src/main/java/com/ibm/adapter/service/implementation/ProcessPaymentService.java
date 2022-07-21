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
import com.ibm.adapter.service.interfaces.IProcessPaymentEngine;
import com.ibm.payment.infra.exception.PaymentConnectionException;

/**
 * ProcessPaymentService class implements from {@link IProcessPaymentEngine}
 * interface, used for processing authorisation payment API. Also, this happens
 * in the following scenarios to create the authorisation: <br>
 * 
 * <ul>
 * <li>Authorisation Expire - Existing authorisation has expired and new
 * authorisation has to be generated.</li>
 * <li>Order modification with an increase in order totals. OMS creates an
 * additional authorisation for the requested amount.</li>
 * </ul>
 */
public class ProcessPaymentService implements IProcessPaymentEngine {

    private static final Logger logger = LogManager.getLogger(ProcessPaymentService.class);

    private PaymentInfraContext paymentInfraContext;

    /**
     * Constructor which injects respective payment service's infra context based on
     * the service type. PaymentInfraContext contains the infrastructure services
     * for the various service types and holds the respective context object from
     * the adapter.
     * 
     * @param paymentInfraContext {@link PaymentInfraContext} object contains
     *                            respective payment infra context for
     *                            ProcessPaymentService.
     */
    public ProcessPaymentService(PaymentInfraContext paymentInfraContext) {
	this.paymentInfraContext = paymentInfraContext;
    }

    /**
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
	logger.debug("ProcessPaymentService - execute()");
	PaymentCollectionOutput paymentCollectionOutput = new PaymentCollectionOutput();
	try {
	    ServiceUtils.setRequestPathParams(requestContext, paymentCollectionInput);
	    paymentCollectionOutput = paymentInfraContext.getProcessPaymentsInfraService().performPayment(
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
     * Method used to check eligibility for processing payments API for
     * authorisation.
     * 
     * @param requestContext         holds {@link RequestContext} object containing
     *                               the request's headers and path parameters.
     * @param paymentCollectionInput holds {@link PaymentCollectionInput} adapter's
     *                               input object is used to validate the
     *                               transaction type based on the service type.
     * @return the boolean value which is eligible for payment processing API.
     */
    @Override
    public boolean isEligible(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
	logger.debug("ProcessPaymentService - isEligible()");
	if ((paymentCollectionInput.getTransactionType().contentEquals(ServiceType.PROCESS_PAYMENTS.toString())
		|| !requestContext.isSkipAuthCall())) {
	    return true;
	}
	return false;
    }
}
