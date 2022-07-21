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

/**
 * This interface has method required which needs to be implemented for the
 * response handler class
 * <ul>
 * <li>Apply Auth Response - converts auth specific response to adapter's output
 * object</li>
 * </ul>
 *
 * @param <T> Payment processor specific http response, typically AdyenResponse
 * @param <J> Adapter's output object, typically PaymentCollectionOutput
 */
public interface IAuthResponseHandler<T, J> {
	/**
	 * This method will be invoked to convert http auth response to
	 * PaymentCollectionOutput
	 * 
	 * Implementors can use response mapper methods for the conversion.
	 * 
	 * @param t Holds AdyenResponse object, authorization response from rest
	 *          HttpClient
	 * @return PaymentCollectionOutput- Adapter's output object
	 */
	J applyAuthResponse(T t);
}
