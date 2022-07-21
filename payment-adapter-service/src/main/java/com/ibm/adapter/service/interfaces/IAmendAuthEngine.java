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
 * IAmendAuthEngine interface involves the processing payment APIs for AmendAuth
 * used for modifying the authorized amount. Whenever there is a modification in
 * the order, we need to increase or decrease the authorized amount.
 * 
 * Typically used {@link PaymentCollectionInput} for generating payment
 * provider's request object and using {@link PaymentCollectionOutput} object
 * for OMS mapping.
 *
 *
 */
public interface IAmendAuthEngine extends IAdapterEngine<PaymentCollectionInput, PaymentCollectionOutput> {

    /**
     * Method which identifies the service type used in the Infra layer to get the
     * required service type for the resources URL construction to invoke the
     * Payment Provider's API.
     * 
     * @return The Service Type as AMEND_AUTHORIZATION.
     */
    default String getServiceType() {
	return ServiceType.AMEND_AUTHORIZATION.toString();
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
     * @return The adapter's output object {@link PaymentCollectionOutput} is used
     *         for OMS mapping.
     */
    PaymentCollectionOutput execute(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);

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
    boolean isEligible(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);
}
