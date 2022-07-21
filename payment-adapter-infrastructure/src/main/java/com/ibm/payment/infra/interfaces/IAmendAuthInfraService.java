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
package com.ibm.payment.infra.interfaces;

import java.util.Map;

import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;

/**
 * This interface has method required which needs to be implemented for the
 * AmendAuthInfraService payment service.
 *
 * @param <I> Payment processor specific input, typically AdyenRequestPathParam
 */
public interface IAmendAuthInfraService<I> {
    /**
     * This method will be used for modifying the existing the authorization.
     * Whenever a modification has been done on the order involving price change ,
     * oms would either need to take additional auth(may only be supported if Adyen
     * allows it) or decrease existing auth.
     * 
     * Implementors can map Generic Model object to Payment Gateway object eg.
     * PaymentCollectionInput to payment processor type request object and this
     * implementation should invoke the amend Authorization API of the payment
     * gateway to get payment processor type response and convert that response back
     * to adapter's output object. <br>
     * example scenarios would be :
     * <ul>
     * <li>Partial order cancellation - Order worth $100 was created . Customer
     * cancels items worth $50 . In this case it's dictated by the payment method
     * configuration to either reverse excess remaining $50 or not.</li>
     * <li>Customer appeasement - Order worth $100 was created. Retailer has
     * provided an appeasement worth $20. In this case retailer does not want to
     * block entire $100 from customer's account.So oms will reverse $20 worth of
     * Authorization.</li>
     * </ul>
     * 
     * @param paymentCollectionInput Holds {@link PaymentCollectionInput} payment
     *                               input object, used for converting the input
     *                               object to payment providers input for amend
     *                               auth payment.
     * @param config                 holds map value containing the request's
     *                               headers and path parameters.
     * @param apiName                Holds API names like payment processing,
     *                               capture, refund, reverse and amend auth. Enum
     *                               AMEND_AUTH is the input for processing the
     *                               amend authorization in the payment adapters.
     * @param requestPathParams      Holds payment provider's request path
     *                               parameters used for amend auth payment
     *                               processing.
     * @return The {@link PaymentCollectionOutput} object,response from Adapter's
     *         output object.
     */
    PaymentCollectionOutput performAmendAuth(PaymentCollectionInput paymentCollectionInput, Map<String, String> config,
	    String apiName, I requestPathParams);
}
