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
 * CaptureInfraService payment service.
 * <ul>
 * <li>perform capture - order/shipment is invoiced and oms wants to
 * charge/capture for the same.</li>
 * </ul>
 *
 * @param <I> Payment processor specific input, typically AdyenRequestPathParam
 */
public interface ICaptureInfraService<I> {
	/**
	 * This method will be invoked by adapter to charge against the payment. Charge
	 * can be done in two ways :
	 * <ul>
	 * <li>Full order charge - If charge amount is equal to authorised amount .E.g :
	 * Order total is $10 and authorised amount is also $10. If implementor wants to
	 * charge for entire $10 , this method should be invoked.</li>
	 * <li>Partial charge - If charge amount is less than authorised amount. e.g:
	 * Order total is $10 and authorised amount is also $10. Oms wants to charge $5
	 * now and want to charge remaining amount later.</li>
	 * </ul>
	 * 
	 * Implementors can use mapper libraries like mapstruct to map from
	 * PaymentCollectionInput to payment processor type request object and pass that
	 * to rest http client to get payment processor type response and convert that
	 * response back to adapter's output object.
	 * 
	 * @param paymentCollectionInput Holds {@link PaymentCollectionInput} payment
	 *                               input object, used for converting the input
	 *                               object to payment providers input for capture
	 *                               payment.
	 * @param config                 holds map value containing the request's
	 *                               headers and path parameters.
	 * @param apiName                Holds API names like payment processing,
	 *                               capture, refund, reverse and amend auth. Enum
	 *                               CAPTURE is the input for processing the capture
	 *                               in the payment adapters.
	 * @param requestPathParams      Holds payment provider's request path
	 *                               parameters used for capture payment processing.
	 * @return The {@link PaymentCollectionOutput} object,response from Adapter's
	 *         output object.
	 */
	PaymentCollectionOutput performCapture(PaymentCollectionInput paymentCollectionInput, Map<String, String> config,
			String apiName, I requestPathParams);
}
