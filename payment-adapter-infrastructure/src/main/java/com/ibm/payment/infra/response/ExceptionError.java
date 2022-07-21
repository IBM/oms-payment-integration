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
package com.ibm.payment.infra.response;

/**
 *
 * ExceptionError enum contains the error code and error messages
 *
 */
public enum ExceptionError {

    PAY0001("0001", "Invalid Request"), 
    PAY0002("0002", "Security Error"), 
    PAY0003("0003", "Invalid Resource"),
    PAY0004("0004", "Server Error"),
    PAY0005("0005", "Incorrect Configuration"),
    PAY0006("0006", "Mapping Error"),
    PAY0007("0007", "Generic Error");

    private String code;
    private String message;

    ExceptionError(String code, String message) {
	this.code = code;
	this.message = message;
    }

    public String getCode() {
	return code;
    }

    public String getMessage() {
	return message;
    }
}
