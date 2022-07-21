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
 * 
 * ICaptureEngine interface used for capturing the authorized payment used for
 * charging and settling the order amount to the respective providers. The
 * charge happens in two ways, Full order and Partial order charges.
 * 
 * Typically used {@link PaymentCollectionInput} for generating payment
 * provider's request object and using {@link PaymentCollectionOutput} object
 * for OMS mapping.
 *
 */
public interface ICaptureEngine extends IAdapterEngine<PaymentCollectionInput, PaymentCollectionOutput> {

    /**
     * Method which identifies the service type used in Infra layer to get the
     * required service type for the resources URL construction to invoke the
     * Payment Provider's API.
     * 
     * @return The Service Type as CAPTURE.
     */
    default String getServiceType() {
	return ServiceType.CAPTURE.toString();
    }

    /**
     * Method used to call the implementation of the Payment Gateway for processing
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
    PaymentCollectionOutput execute(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);

    /**
     * Method used to check eligibility for processing capture payment API.
     * 
     * @param requestContext         holds {@link RequestContext} object containing
     *                               the request's headers and path parameters.
     * @param paymentCollectionInput holds {@link PaymentCollectionInput} adapter's
     *                               input object is used to validate the
     *                               transaction type based on the service type.
     * @return the boolean value which is eligible for processing capture payment
     *         API.
     */
    boolean isEligible(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);
}
