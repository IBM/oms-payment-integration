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
 * IRefundEngine interface, which involved the process of Refunding the
 * authorised payment in the scenario of returning the order items.
 * 
 * Typically used {@link PaymentCollectionInput} for generating payment
 * provider's request object and using {@link PaymentCollectionOutput} object
 * for OMS mapping.
 */
public interface IRefundEngine extends IAdapterEngine<PaymentCollectionInput, PaymentCollectionOutput> {

    /**
     * Method which identifies the service type used in Infra layer to get the
     * required service type for the resources URL construction to invoke the
     * Payment Provider's API.
     * 
     * @return The Service Type as REFUND.
     */
    default String getServiceType() {
	return ServiceType.REFUND.toString();
    }

    /**
     * This method will be invoked by adapter for all the refund scenarios. <br>
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
    PaymentCollectionOutput execute(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);

    /**
     * Method used to check eligibility for processing Refund payment API.
     * 
     * @param requestContext         holds {@link RequestContext} object containing
     *                               the request's headers and path parameters.
     * @param paymentCollectionInput holds {@link PaymentCollectionInput} adapter's
     *                               input object is used to validate the
     *                               transaction type based on the service type.
     * @return the boolean value which is eligible for processing refund payment
     *         API.
     */
    boolean isEligible(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);

}
