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

import com.ibm.adapter.context.RequestContext;
import com.ibm.adapter.util.ServiceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.adapter.context.PaymentInfraContext;
import com.ibm.adapter.payment.enums.ServiceType;
import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.adapter.service.interfaces.IAmendAuthEngine;
import com.ibm.payment.infra.exception.PaymentConnectionException;

/**
 * AmendAuthService implements from {@link IAmendAuthEngine} interface, used for
 * processing the Amend Auth payment APIs used for increasing or decreasing the
 * authorized amount. Whenever there is a modification in the order, we need to
 * increase or decrease the authorized amount.
 *
 */
public class AmendAuthService implements IAmendAuthEngine {

    private static final Logger logger = LogManager.getLogger(AmendAuthService.class);

    private PaymentInfraContext paymentInfraContext;

    /**
     * Constructor which injects respective payment service's infra context based on
     * the service type. PaymentInfraContext contains the infrastructure services
     * for the various service types and holds the respective context object from
     * the adapter.
     * 
     * @param paymentInfraContext {@link PaymentInfraContext} object contains
     *                            respective payment infra context for
     *                            AmendAuthService.
     */
    public AmendAuthService(PaymentInfraContext paymentInfraContext) {
	this.paymentInfraContext = paymentInfraContext;
    }

    /**
     * This method is used for modifying the existing authorisation. Whenever
     * modification in the order involves price change, OMS would either need to
     * take additional auth or decrease existing auth. <br>
     * example scenarios would be :
     * <ul>
     * <li>Partial order cancellation - Order worth $100 was created . Customer
     * cancels items worth $50. In this case, OMS might choose to reverse the
     * remaining $50.</li>
     * <li>Customer appeasement - Order worth $100 was created. The retailer has
     * provided appeasement worth $20. In this case, the retailer does not want to
     * block the entire $100 from the customer's account. So OMS will reverse $20
     * worth of authorisation.</li>
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
	logger.debug("AmendAuthService - execute()");
	PaymentCollectionOutput paymentCollectionOutput = new PaymentCollectionOutput();
	try {
	    ServiceUtils.setRequestPathParams(requestContext, paymentCollectionInput);
	    paymentCollectionOutput = paymentInfraContext.getAmountUpdatesInfraService().performAmendAuth(
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
     * Method used to check eligibility for processing AmendAuth payment API.
     * 
     * @param requestContext         holds {@link RequestContext} object containing
     *                               the request's headers and path parameters.
     * @param paymentCollectionInput holds {@link PaymentCollectionInput} adapter's
     *                               input object is used to validate the
     *                               transaction type based on the service type.
     * @return the boolean value which is eligible for processing AmendAuth payment
     *         API.
     */
    @Override
    public boolean isEligible(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
	logger.debug("AmendAuthService - isEligible()");
	if ((paymentCollectionInput.getTransactionType().contentEquals(ServiceType.AMEND_AUTHORIZATION.toString())
		|| !requestContext.isSkipAmountUpdatesCall())) {
	    return true;
	}
	return false;
    }

}
