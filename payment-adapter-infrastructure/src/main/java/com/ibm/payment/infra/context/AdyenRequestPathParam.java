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
package com.ibm.payment.infra.context;

import lombok.NonNull;

/**
 * This object holds adyen specific details which will be helpful while creating
 * adyen request body, url's etc.
 * 
 */
public class AdyenRequestPathParam {
    /** PSPReference */
    @NonNull
    private String pspPaymentReference;

    /** PSPCaptureReference */
    private String pspCaptureReference;

    /** PSPReverseReference */
    private String pspReverseReference;

    public String getPspPaymentReference() {
	return pspPaymentReference;
    }

    public void setPspPaymentReference(String pspPaymentReference) {
	this.pspPaymentReference = pspPaymentReference;
    }

    public String getPspCaptureReference() {
	return pspCaptureReference;
    }

    public void setPspCaptureReference(String pspCaptureReference) {
	this.pspCaptureReference = pspCaptureReference;
    }

    public String getPspReverseReference() {
	return pspReverseReference;
    }

    public void setPspReverseReference(String pspReverseReference) {
	this.pspReverseReference = pspReverseReference;
    }
}
