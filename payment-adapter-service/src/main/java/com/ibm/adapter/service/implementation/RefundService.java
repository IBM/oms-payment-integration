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
import com.ibm.adapter.service.interfaces.IRefundEngine;
import com.ibm.payment.infra.exception.PaymentConnectionException;

/**
 * RefundService class implements from {@link IRefundEngine} interface, which
 * involved the process of Refunding the captured payment in the scenario of
 * returning the order items. This refund happens in two ways <br>
 * <ul>
 * <li>Full refund - A order created with two items. The customer returns the
 * entire order.</li>
 * <li>Partial refund - A order created with two items, the customer wants to
 * return an item</li>
 * </ul>
 * 
 */
public class RefundService implements IRefundEngine {

    private static final Logger logger = LogManager.getLogger(RefundService.class);

    private PaymentInfraContext paymentInfraContext;

    /**
     * Constructor which injects respective payment service's infra context based on
     * the service type. PaymentInfraContext contains the infrastructure services
     * for the various service types and holds the respective context object from
     * the adapter.
     * 
     * @param paymentInfraContext {@link PaymentInfraContext} object contains
     *                            respective payment infra context for
     *                            RefundService.
     */
    public RefundService(PaymentInfraContext paymentInfraContext) {
	this.paymentInfraContext = paymentInfraContext;
    }

    /**
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
	logger.debug("RefundService - execute()");
	PaymentCollectionOutput paymentCollectionOutput = new PaymentCollectionOutput();
	try {
	    ServiceUtils.setRequestPathParams(requestContext, paymentCollectionInput);
	    paymentCollectionOutput = paymentInfraContext.getRefundInfraService().performRefund(paymentCollectionInput,
		    requestContext.getProperties(), getServiceType(), requestContext.getRequestPathParams());
	} catch (PaymentConnectionException e) {
	    ServiceUtils.handlePaymentConnectionException(paymentCollectionOutput, e);
	} catch (Exception e) {
	    ServiceUtils.handleGenericException(e);
	}
	return paymentCollectionOutput;
    }

    /**
     * Method used to check eligibility for processing refund payment API.
     * 
     * @param requestContext         holds {@link RequestContext} object containing
     *                               the request's headers and path parameters.
     * @param paymentCollectionInput holds {@link PaymentCollectionInput} adapter's
     *                               input object is used to validate the
     *                               transaction type based on the service type.
     * @return the boolean value which is eligible for payment refund API.
     */
    @Override
    public boolean isEligible(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
	logger.debug("RefundService - isEligible()");
	if ((paymentCollectionInput.getTransactionType().contentEquals(ServiceType.REFUND.toString())
		|| !requestContext.isSkipRefundCall())) {
	    return true;
	}
	return false;
    }
}
