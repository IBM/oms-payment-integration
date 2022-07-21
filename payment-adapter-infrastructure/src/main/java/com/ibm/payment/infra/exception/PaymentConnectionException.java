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

/**
 * <ul>
 * <li>Custom Exceptions created extends from RuntimeException used to handle
 * the connectivity issues.</li>
 * 
 * <li>It enriches them with a custom error code and error messages to identify
 * the corresponding errors.</li>
 * </ul>
 * 
 */
public class PaymentConnectionException extends RuntimeException {

    /**
     * 
     * serial Version ID
     */
    private static final long serialVersionUID = 1L;

    public String errorCode;
    public String errorMessage;

    public PaymentConnectionException(String errorMessage) {
	setErrorMessage(errorMessage);
    }

    public PaymentConnectionException(String errorCode, String errorMessage) {
	setErrorCode(errorCode);
	setErrorMessage(errorMessage);
    }

    public PaymentConnectionException(String errorMessage, Throwable e) {
	setErrorMessage(errorMessage);
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

}
