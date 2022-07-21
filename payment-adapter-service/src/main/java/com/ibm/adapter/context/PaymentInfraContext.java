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

import com.ibm.payment.infra.interfaces.IAmendAuthInfraService;
import com.ibm.payment.infra.interfaces.IReverseInfraService;
import com.ibm.payment.infra.interfaces.ICaptureInfraService;
import com.ibm.payment.infra.interfaces.IProcessPaymentsInfraService;
import com.ibm.payment.infra.interfaces.IRefundInfraService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * PaymentInfraContext class contains the payment provider's specific
 * infrastructure services (i.e) Adyen Infraservices used to invoke the
 * respective API based on the service types like process payment, reverse,
 * refund, capture and amendauth. The various services specific to Adyen payment
 * gateways are<br>
 * <ul>
 * <li>{@link ICaptureInfraService}</li>
 * <li>{@link IReverseInfraService}</li>
 * <li>{@link IRefundInfraService}</li>
 * <li>{@link IAmendAuthInfraService}</li>
 * <li>{@link IProcessPaymentsInfraService}</li>
 * </ul>
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfraContext {

    ICaptureInfraService captureInfraService;
    IReverseInfraService reverseInfraService;
    IRefundInfraService refundInfraService;
    IAmendAuthInfraService amountUpdatesInfraService;
    IProcessPaymentsInfraService processPaymentsInfraService;
}
