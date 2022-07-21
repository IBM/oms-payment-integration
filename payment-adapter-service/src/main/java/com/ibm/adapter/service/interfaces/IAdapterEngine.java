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

/**
 * IAdapterEngine interface used for processing the payment APIs based on the
 * service types. This interface extends by different services listed below for
 * processing respective payment services.
 * 
 * <ul>
 * <li>{@link IProcessPaymentEngine}</li>
 * <li>{@link IAmendAuthEngine}</li>
 * <li>{@link ICaptureEngine}</li>
 * <li>{@link IReverseEngine}</li>
 * <li>{@link IRefundEngine}</li>
 * </ul>
 * 
 * @param <T> Client specific input, typically adapter's input used for
 *            generating payment provider's request object.
 * @param <V> Client specific output, typically adapter's input used for OMS
 *            mapping.
 * 
 */
public interface IAdapterEngine<T, V> {

    /**
     * Method used to call Infrastructure layer for processing payment providers
     * based on the service type(process payment, capture, amendauth, reverse auth,
     * refund).
     * 
     * @param requestContext holds {@link RequestContext} request context object
     *                       containing the request's headers and path parameters.
     * @param t              adapter's input which used to construct the request
     *                       based on the payment provider's request.
     * @return The adapter's output is used for OMS mapping.
     */
    V execute(RequestContext requestContext, T t);

    /**
     * Method used to check eligibility for process payments API.
     * 
     * @param requestContext request context object.
     * @param t              adapter's input is used to check the transaction type
     *                       based on the service type.
     * @return the boolean value which is eligible for payment processing API.
     */
    boolean isEligible(RequestContext requestContext, T t);
}
