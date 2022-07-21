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
 * RefundInfraService payment service.
 * <ul>
 * <li>Perform refund - order is invoiced and reached its final state.But
 * customer has either returned the item or has been given some appeasement.Oms
 * has to refund money back to customer in this case.</li>
 * </ul>
 *
 * @param <I> Payment processor specific input, typically AdyenRequestPathParam
 */
public interface IRefundInfraService<I> {
	/**
	 * This method will be invoked by adapter for all the refund scenarios. <br>
	 * <ul>
	 * <li>Full refund - Order was created for $100 . Customer returns the entire
	 * order.</li>
	 * <li>Partial refund - Order was created for $100 , customer wants to return an
	 * item worth $40</li>
	 * </ul>
	 * 
	 * Implementors can map Generic Model object to Payment Gateway object eg.
	 * PaymentCollectionInput to payment processor type request object and this
	 * implementation should invoke the amend Authorization API of the payment
	 * gateway to get payment processor type response and convert that response back
	 * to adapter's output object. <br>
	 * 
	 * @param paymentCollectionInput Holds {@link PaymentCollectionInput} payment
	 *                               input object, used for converting the input
	 *                               object to payment providers input for refund
	 *                               payment.
	 * @param config                 holds map value containing the request's
	 *                               headers and path parameters.
	 * @param apiName                Holds API names like payment processing,
	 *                               capture, refund, reverse and amend auth. Enum
	 *                               REFUND is the input for processing the refund
	 *                               in the payment adapters.
	 * @param requestPathParams      Holds payment provider's request path
	 *                               parameters used for processing refund payment .
	 * @return The {@link PaymentCollectionOutput} object,response from Adapter's
	 *         output object.
	 */
	PaymentCollectionOutput performRefund(PaymentCollectionInput paymentCollectionInput, Map<String, String> config,
			String apiName, I requestPathParams);
}
