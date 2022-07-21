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

import com.ibm.adapter.context.PaymentServiceContext;

/**
 * IPaymentServiceInjector interface provides methods for getting Service
 * context objects based on the service types.
 * 
 * The {@link PaymentServiceContext} contains the services for various service
 * types like process payment, amendauth, capture, refund, and reverse auth.
 * 
 * Also, this interface is used for implementing any Service Injector depending
 * on the payment gateways used for injecting the service implementation class.
 * 
 */
public interface IPaymentServiceInjector {

    /**
     * Method used to get the payment service context of various service types helps
     * in invoking the services layers.
     * 
     * @return The {@link PaymentServiceContext} object the service implementation
     *         for various transaction types.
     */
    public PaymentServiceContext getServiceContext();
}
