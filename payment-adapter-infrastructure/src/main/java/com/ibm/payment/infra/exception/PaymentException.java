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

import org.json.JSONObject;

import com.ibm.payment.infra.response.ExceptionError;

/**
 * <ul>
 * <li>Custom Exceptions created extends from RuntimeException used to handle
 * the payment related general errors.</li>
 * 
 * <li>PaymentConfigurationException enriches them with a custom error code and
 * error messages to identify the corresponding errors.</li>
 * </ul>
 *
 */
public class PaymentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final String ERROR_CODE = "ErrorCode";

    public static final String ERROR_MESSAGE = "ErrorMessage";

    public static final String ERROR_ATTRIBUTES = "ErrorAttributes";

    public static final String PAY = "PAY";

    public String errorCode;

    public String errorMessage;

    public JSONObject errorAttributes;

    public PaymentException(String errorCode, String errorMessage) {
	super();
	setErrorCode(errorCode);
	setErrorMessage(errorMessage);
    }

    public PaymentException(ExceptionError error, JSONObject errorAttributes) {
	this(PAY + error.getCode(), error.getMessage(), errorAttributes);

    }

    public PaymentException(String errorCode, String errorMessage, JSONObject errorAttributes) {
	super();
	setErrorCode(errorCode);
	setErrorMessage(errorMessage);
	setErrorAttributes(errorAttributes);
    }

    public PaymentException(Throwable ex) {
	this(ex, ex.getClass().getName());
    }

    public PaymentException(Throwable ex, String errorCode) {
	setErrorCode(errorCode);
	setErrorMessage(ex.getMessage());
    }

    public String getErrorCode() {
	return errorCode;
    }

    public void setErrorCode(String errorCode) {
	this.errorCode = errorCode;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public JSONObject getErrorAttributes() {
	return errorAttributes;
    }

    public void setErrorAttributes(JSONObject errorAttributes) {
	this.errorAttributes = errorAttributes;
    }

    public String getAttributes(String name) {
	return (String) this.errorAttributes.get(name);
    }

    @Override
    public String getMessage() {
	String message = super.getMessage();
	String string = getString(message);
	return (string == null) ? message : string;
    }

    private String getString(String message) {
	JSONObject responseJSON = new JSONObject();
	responseJSON.put(ERROR_CODE, this.errorCode);
	if (this.errorAttributes != null) {
	    responseJSON.put(ERROR_ATTRIBUTES, this.errorAttributes);
	}
	if (message != null) {
	    responseJSON.put(ERROR_MESSAGE, this.errorMessage + message);
	} else {
	    responseJSON.put(ERROR_MESSAGE, this.errorMessage);
	}
	return responseJSON.toString();

    }
}
