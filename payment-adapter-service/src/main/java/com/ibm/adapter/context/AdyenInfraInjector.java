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
package com.ibm.adapter.context;

import com.ibm.adapter.service.interfaces.PaymentInfraInjector;
import com.ibm.payment.infra.implementation.AmendAuthInfraService;
import com.ibm.payment.infra.implementation.ReverseInfraService;
import com.ibm.payment.infra.implementation.CaptureInfraService;
import com.ibm.payment.infra.implementation.ProcessPaymentsInfraService;
import com.ibm.payment.infra.implementation.RefundInfraService;

/**
 * This class implements from {@link PaymentInfraContext} interface, holds the
 * implementation class for invoking capture, reverseauth, amend auth, refund
 * interfaces specific to Adyen payment gateway.
 * 
 */
public class AdyenInfraInjector implements PaymentInfraInjector {

    private PaymentInfraContext paymentInfraContext;

    /**
     * 
     * Method used to get the payment infracontext which injects various Adyen's
     * infra services for processing the payment.
     * 
     * {@link PaymentInfraContext} which holds the various infraservices which are
     * listed below<br>
     * <ul>
     * <li>{@link CaptureInfraService}</li>
     * <li>{@link ReverseInfraService}</li>
     * <li>{@link RefundInfraService}</li>
     * <li>{@link AmendAuthInfraService}</li>
     * <li>{@link ProcessPaymentsInfraService}</li>
     * </ul>
     * 
     * This is specific to Adyen payment gateways and invokes the respective API for
     * processing the payment API.
     * 
     * @return The {@link PaymentInfraContext} objects contain the various infra
     *         services based on the different service types.
     */
    @Override
    public PaymentInfraContext getInfraContext() {
	paymentInfraContext = new PaymentInfraContext(new CaptureInfraService(), new ReverseInfraService(),
		new RefundInfraService(), new AmendAuthInfraService(), new ProcessPaymentsInfraService());
	return paymentInfraContext;
    }
}
