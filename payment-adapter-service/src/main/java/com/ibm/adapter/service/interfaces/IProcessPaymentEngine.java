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
package com.ibm.adapter.service.interfaces;

import com.ibm.adapter.context.RequestContext;
import com.ibm.adapter.payment.enums.ServiceType;
import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;

/**
 * IProcessPaymentEngine interface used for processing payment API for
 * authorisation. Also, this happens in the following scenarios to create the
 * authorisation: <br>
 * 
 * <ul>
 * <li>Authorisation Expire - Existing authorisation has expired and new
 * authorisation has to be generated.</li>
 * <li>Order modification with an increase in order totals. Oms creates an
 * additional authorisation for the requested amount.</li>
 * </ul>
 * 
 * Typically used {@link PaymentCollectionInput} for generating payment
 * provider's request object and using {@link PaymentCollectionOutput} object
 * for OMS mapping.
 */
public interface IProcessPaymentEngine extends IAdapterEngine<PaymentCollectionInput, PaymentCollectionOutput> {

    /**
     * Method which identifies the service type used in Infra layer to get the
     * required service type for the resources URL construction to invoke the
     * Payment Provider's API.
     * 
     * @return The Service Type as CAPTURE.
     */
    default String getServiceType() {
	return ServiceType.PROCESS_PAYMENTS.toString();
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
    PaymentCollectionOutput execute(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);

    /**
     * Method used to check eligibility for processing payments API for
     * Authorization.
     * 
     * @param requestContext         holds {@link RequestContext} object containing
     *                               the request's headers and path parameters.
     * @param paymentCollectionInput holds {@link PaymentCollectionInput} adapter's
     *                               input object is used to validate the
     *                               transaction type based on the service type.
     * @return the boolean value which is eligible for payment processing API.
     */
    boolean isEligible(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);

}
