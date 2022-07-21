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

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * ExternalPaymentInput model class holds the payment input information
 *
 */
@Getter
@Setter
public class ExternalPaymentInput {
    private String authorizationId;
    private String paymentReference5;
    private String paymentReference6;
    private String paymentReference7;
    private String orderNo;
    private String requestAmount;
    private String currentAuthorizationAmount;
    private String paymentKey;
    private String currency;
    private String chargeType;
}
