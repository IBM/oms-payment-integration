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

import com.ibm.adapter.context.PaymentInfraContext;

/**
 * PaymentInfraInjector interface provides methods for getting infrastructure
 * context objects based on the payment providers.
 * 
 * The {@link PaymentInfraContext} contains the InfraServices for various
 * service types like process payment, amendauth, capture, refund, and reverse
 * auth.
 * 
 * Also, this method is used to get all the infrastructure services objects for
 * various service types and helps in invoking the rest API depending on the
 * payment providers.
 * 
 * Also, this interface is used for implementing any Infra Injector depending on
 * the payment gateways used for injecting the Infra services implementation
 * class.
 * 
 * 
 */
public interface PaymentInfraInjector {

    /**
     * Method used to get the payment infracontext based on the payment gateway
     * which inject various infrastructure services for processing the payment API
     * based on the service type.
     * 
     * @return The {@link PaymentInfraContext} objects which contains the infra
     *         services.
     */
    public PaymentInfraContext getInfraContext();
}
