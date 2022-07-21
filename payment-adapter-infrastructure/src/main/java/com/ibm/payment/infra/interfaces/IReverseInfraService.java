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
 * ReverseInfraService payment service.
 * <ul>
 * <li>perform cancel - Entire Order has been reversed before fulfillment, so
 * oms will reverse the entire authorised amount.</li>
 * </ul>
 *
 * @param <I> Payment processor specific input, typically AdyenRequestPathParam
 */
public interface IReverseInfraService<I> {
    /**
     * This method will be invoked by the adapter during full order cancellation
     * scenarios. <strong>This method should be used if and only if the order is not
     * charged.Incase if the order is charged, then performRefund should be
     * used</strong> <br>
     * e.g: Order was created for a total of $10 and it was cancelled.
     * 
     * Implementors can map Generic Model object to Payment Gateway object eg.
     * PaymentCollectionInput to payment processor type request object and this
     * implementation should invoke the amend Authorization API of the payment
     * gateway to get payment processor type response and convert that response back
     * to adapter's output object. <br>
     * 
     * @param paymentCollectionInput Holds {@link PaymentCollectionInput} payment
     *                               input object, used for converting the input
     *                               object to payment providers input for reverse
     *                               payment.
     * @param config                 holds map value containing the request's
     *                               headers and path parameters.
     * @param apiName                Holds API names like payment processing,
     *                               capture, refund, reverse and amend auth. Enum
     *                               REVERSE is the input for processing the reverse
     *                               in the payment adapters.
     * @param requestPathParams      Holds payment provider's request path
     *                               parameters used for processing reverse payment
     *                               .
     * @return The {@link PaymentCollectionOutput} object,response from Adapter's
     *         output object.
     */
    PaymentCollectionOutput performReverseAuth(PaymentCollectionInput paymentCollectionInput,
	    Map<String, String> config, String apiName, I requestPathParams);
}
