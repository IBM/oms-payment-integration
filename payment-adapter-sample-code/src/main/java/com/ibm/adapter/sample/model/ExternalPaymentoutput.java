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
package com.ibm.adapter.sample.model;

import lombok.Data;

/**
 * 
 * ExternalPaymentInput model class holds the payment output information
 *
 */
@Data
public class ExternalPaymentoutput {
    String authorizationId;
    String paymentReference5;
    String paymentReference6;
    String paymentReference7;
    String orderNo;
    String requestAmount;
    String currentAuthorizationAmount;
    String paymentKey;
    String asynchRequestProcess;
    String asyncRequestIdentifier;
}
