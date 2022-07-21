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
package com.ibm.payment.infra.requestHandlers.interfaces;

import java.util.Map;

/**
 * <ul>
 * <li>RequestHandler interface provide methods to implement request handler for
 * various payment gateways.</li>
 * 
 * <li>This interface contains the methods to fetch the resources URL, request
 * headers and request configurations(socket timeout, connection request
 * timeout, connect timeout) used in invoking API based on the payment
 * gateways.</li>
 * </ul>
 * 
 * @param <IRequest>                Holds the Request object of the payment
 *                                  providers.
 * @param <IRequestHeaderParameter> Indicates the RequestPathParameters object
 *                                  of the payment providers.
 * @param <IRequestConfigs>         Holds the payment provider's RequestConfig
 *                                  object.
 */
public interface RequestHandler<IRequest, IRequestHeaderParameter, IRequestConfigs> {

    /**
     * Methods to get the request header parameters are required for payment
     * gateways to invoke the respective API.
     * 
     * @param requestConfig Holds the request config map which used to set headers.
     * @param request       Indicates the request object.
     * @return The header maps are based on the payment providers.
     */
    public Map<String, String> getHeader(Map<String, String> requestConfig, IRequest request);

    /**
     * Method used to construct the resources URL based on the payment providers.
     * 
     * @param apiName           Holds API names like payment processing, capture,
     *                          refund, reverse and amendauth.
     * @param requestPathParams Holds request path parameters object.
     * @return The constructed URL is based on the payment providers.
     */
    public String getResourceURL(String apiName, IRequestHeaderParameter requestPathParams);

    /**
     * Method used to get the request configurations like socket timeout, connection
     * request timeout, and connect timeout used for setting the timeout request
     * configs in Httpclient.
     * 
     * @return The RequestConfigs based on the payment providers.
     */
    public IRequestConfigs getRequestConfig();

}
