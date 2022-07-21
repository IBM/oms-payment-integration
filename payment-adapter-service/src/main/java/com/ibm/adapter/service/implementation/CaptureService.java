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

import com.ibm.adapter.service.interfaces.ICaptureEngine;
import com.ibm.adapter.util.ServiceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.adapter.context.PaymentInfraContext;
import com.ibm.adapter.context.RequestContext;
import com.ibm.adapter.payment.enums.ServiceType;
import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.payment.infra.exception.PaymentConnectionException;

/**
 * CaptureService class implements from {@link ICaptureEngine} interface, used
 * for Capturing the authorised payment used for charging and settling the order
 * amount to the respective providers. The charge happens in two ways, Full
 * order and Partial order charges.
 * 
 */
public class CaptureService implements ICaptureEngine {

    private static final Logger logger = LogManager.getLogger(CaptureService.class);

    private PaymentInfraContext paymentInfraContext;

    /**
     * Constructor which injects respective payment service's infra context based on
     * the service type. PaymentInfraContext contains the infrastructure services
     * for the various service types and holds the respective context object from
     * the adapter.
     * 
     * @param paymentInfraContext {@link PaymentInfraContext} object contains
     *                            respective payment infra context for
     *                            CaptureService.
     */
    public CaptureService(PaymentInfraContext paymentInfraContext) {
	this.paymentInfraContext = paymentInfraContext;
    }

    /**
     * Method used to call the Implementation of the Payment Gateway for processing
     * capture payment API. Also invoked by an adapter to charge and settle the
     * amount against the payment. The charge happens in two ways :
     * <ul>
     * <li>Full order charge - If charge amount == authorised amount .E.g : Order
     * total is $10 and authorised amount is also $10. If the implementor wants to
     * charge for the entire $10, this method should be invoked.</li>
     * <li>Partial charge - If charge amount is less than authorised amount. for
     * example, the order total is $10, and the authorised amount is $10. Oms wants
     * to charge $5 now and needs to charge the remaining amount later.</li>
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
	logger.debug("CaptureService - execute()");
	PaymentCollectionOutput paymentCollectionOutput = new PaymentCollectionOutput();
	try {
	    ServiceUtils.setRequestPathParams(requestContext, paymentCollectionInput);
	    paymentCollectionOutput = paymentInfraContext.getCaptureInfraService().performCapture(
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
     * Method used to check eligibility for processing capture payment API.
     * 
     * @param requestContext         holds {@link RequestContext} object containing
     *                               the request's headers and path parameters.
     * @param paymentCollectionInput holds {@link PaymentCollectionInput} adapter's
     *                               input object is used to validate the
     *                               transaction type based on the service type.
     * @return the boolean value which is eligible for payment capture API.
     */
    @Override
    public boolean isEligible(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
	logger.debug("CaptureService - isEligible()");
	if ((paymentCollectionInput.getTransactionType().contentEquals(ServiceType.CAPTURE.toString())
		|| !requestContext.isSkipCaptureCall())) {
	    return true;
	}
	return false;
    }
}
