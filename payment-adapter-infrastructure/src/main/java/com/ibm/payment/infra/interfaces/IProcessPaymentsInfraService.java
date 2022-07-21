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
 * ProcessPaymentsInfraService payment service.
 * <ul>
 * <li>perform payment - Oms wants to take a new authorisation because either
 * the old auth has expired or take a new auth for a new charge.</li>
 * </ul>
 *
 * @param <I> Payment processor specific input, typically AdyenRequestPathParam
 */
public interface IProcessPaymentsInfraService<I> {
	/**
	 * This method should be used in following scenarios: <br>
	 * <ul>
	 * <li>Auth expiry - Existing authorisation has expired and new authorisation
	 * has to be generated.</li>
	 * <li>Order modification with increase in order totals - Order worth $200 was
	 * created. Customer has added additional items to the order and the order total
	 * is now $250.Oms creates an additional authorisation request for $50.</li>
	 * </ul>
	 * 
	 * Implementors can map Generic Model object to Payment Gateway object eg.
	 * PaymentCollectionInput to payment processor type request object and this
	 * implementation should invoke the amend Authorization API of the payment
	 * gateway to get payment processor type response and convert that response back
	 * to adapter's output object. <br>
	 * 
	 * 
	 * @param paymentCollectionInput Holds {@link PaymentCollectionInput} payment
	 *                               input object, used for converting the input
	 *                               object to payment providers input for payment
	 *                               authorization.
	 * @param config                 holds map value containing the request's
	 *                               headers and path parameters.
	 * @param apiName                Holds API names like payment processing,
	 *                               capture, refund, reverse and amend auth. Enum
	 *                               PROCESS_PAYMENTS is the input for payment
	 *                               authorization in the payment adapters.
	 * @param requestPathParams      Holds payment provider's request path
	 *                               parameters used for payment authorization.
	 * @return The {@link PaymentCollectionOutput} object,response from Adapter's
	 *         output object.
	 */

	PaymentCollectionOutput performPayment(PaymentCollectionInput paymentCollectionInput, Map<String, String> config,
			String apiName, I requestPathParams);
}
