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

import com.ibm.adapter.service.implementation.AmendAuthService;
import com.ibm.adapter.service.implementation.ReverseService;
import com.ibm.adapter.service.implementation.CaptureService;
import com.ibm.adapter.service.implementation.ProcessPaymentService;
import com.ibm.adapter.service.implementation.RefundService;
import com.ibm.adapter.service.interfaces.IPaymentServiceInjector;

/**
 * 
 * GenericPaymentServiceInjector class implemented from
 * {@link IPaymentServiceInjector} interface, holds the implementations for
 * IAmendAuthEngine, IReverseEngine, ICaptureEngine, IRefundEngine.
 * 
 * This class will be injected into adapter which is specific to Adyen Payment
 * Gateway.
 * 
 */
public class GenericPaymentServiceInjector implements IPaymentServiceInjector {

    private PaymentInfraContext paymentInfraContext;

    /**
     * Constructor used for injecting the {@link PaymentInfraContext} object along
     * with @link {@link PaymentServiceContext}. Here configured the
     * {@link AdyenInfraInjector} object which is specific to Adyen Payment Gateway.
     * 
     * 
     * Return The {@link GenericPaymentServiceInjector} object, also inject the
     * {@link AdyenInfraInjector} to the {@link PaymentInfraContext} object for
     * invoking respective payment providers.
     */
    public GenericPaymentServiceInjector() {
	this.paymentInfraContext = new AdyenInfraInjector().getInfraContext();
    }

    /**
     * Method used to get the payment service context which injects various services
     * along with Adyen's payment infracontext for processing the payment.
     * {@link PaymentInfraContext} which holds the various infraservices which are
     * listed below<br>
     * <ul>
     * <li>{@link AmendAuthService}</li>
     * <li>{@link ReverseService}</li>
     * <li>{@link CaptureService}</li>
     * <li>{@link RefundService}</li>
     * <li>{@link ProcessPaymentService}</li>
     * </ul>
     * 
     * This is specific to Adyen payment gateways and invokes the respective API for
     * processing the payment API.
     * 
     * @return The {@link PaymentServiceContext} objects contain the various service
     *         implementation along with payment infracontext depending on payment
     *         providers for several transaction types.
     */
    @Override
    public PaymentServiceContext getServiceContext() {
	return new PaymentServiceContext(new AmendAuthService(paymentInfraContext),
		new ReverseService(paymentInfraContext), new CaptureService(paymentInfraContext),
		new RefundService(paymentInfraContext), new ProcessPaymentService(paymentInfraContext));
    }
}
