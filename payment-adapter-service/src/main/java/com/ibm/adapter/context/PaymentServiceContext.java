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

import com.ibm.adapter.service.interfaces.IReverseEngine;
import com.ibm.adapter.service.interfaces.ICaptureEngine;
import com.ibm.adapter.service.interfaces.IProcessPaymentEngine;
import com.ibm.adapter.service.interfaces.IAmendAuthEngine;
import com.ibm.adapter.service.interfaces.IRefundEngine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <ul>
 * <li>PaymentServiceContext class contains the various services based on
 * service types used to invoke the respective payment gateways API.</li>
 * 
 * <li>This context object can be used to pass context data from invoking
 * layers.</li>
 * 
 * <li>The service layer holds the implementation class of process payment,
 * reverse, refund, capture and amend auth.The various services are<br>
 * </li>
 * </ul>
 * 
 * <ul>
 * <li>{@link IAmendAuthEngine}</li>
 * <li>{@link IReverseEngine}</li>
 * <li>{@link ICaptureEngine}</li>
 * <li>{@link IRefundEngine}</li>
 * <li>{@link IProcessPaymentEngine}</li>
 * </ul>
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentServiceContext {

    IAmendAuthEngine amendAuthEngine;
    IReverseEngine reverseEngine;
    ICaptureEngine captureEngine;
    IRefundEngine refundEngine;
    IProcessPaymentEngine processPaymentEngine;
}
