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
package com.ibm.payment.infra.exception;

import com.ibm.payment.infra.response.ExceptionError;
import org.json.JSONObject;

/**
 * <ul>
 * <li>Custom Exceptions created extends from RuntimeException used to handle
 * the errors while doing payment-related mappings.</li>
 * 
 * <li>Thrown to indicate that a method has passed an illegal or inappropriate
 * argument in mapping the payment attributes.</li>
 * 
 * <li>It enriches them with a custom error code and error messages to identify
 * the respective errors.</li>
 * </ul>
 * 
 */
public class PaymentMappingException extends RuntimeException {

    /**
     * serial Version ID
     */
    private static final long serialVersionUID = 1L;

    public static final String PAYMAP = "PAYMAP";

    public String errorCode;
    public String errorMessage;
    public JSONObject errorAttributes;

    public PaymentMappingException(String errorCode, String errorMessage) {
	super();
	setErrorCode(errorCode);
	setErrorMessage(errorMessage);
    }

    public PaymentMappingException(ExceptionError error, JSONObject errorAttributes) {
	this(PAYMAP + error.getCode(), error.getMessage(), errorAttributes);

    }

    public PaymentMappingException(String errorCode, String errorMessage, JSONObject errorAttributes) {
	super();
	setErrorCode(errorCode);
	setErrorMessage(errorMessage);
	setErrorAttributes(errorAttributes);
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
	return errorCode;
    }

    public void setErrorCode(String errorCode) {
	this.errorCode = errorCode;
    }

    public JSONObject getErrorAttributes() {
	return errorAttributes;
    }

    public void setErrorAttributes(JSONObject errorAttributes) {
	this.errorAttributes = errorAttributes;
    }
}
