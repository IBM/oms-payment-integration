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
 * RestHttpClientResponse class holds the response code and message. In
 * StandardHttpClient, it is used to set the response from various payment
 * gateways.
 *
 */
public class RestHttpClientResponse {

    private int responseCode;

    private String response;

    public int getResponseCode() {
	return responseCode;
    }

    public void setResponseCode(int responseCode) {
	this.responseCode = responseCode;
    }

    public String getResponse() {
	return response;
    }

    public void setResponse(String response) {
	this.response = response;
    }

}
