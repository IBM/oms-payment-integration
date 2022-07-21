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
package com.ibm.payment.infra.client;

import java.util.Map;

import com.ibm.payment.infra.response.RestHttpClientResponse;

/**
 * <ul>
 * <li>IRestHttpClient interface provides methods for invoking APIs which helps
 * in implementing various HttpClients like HttpUrlConnection,
 * CloseableHttpClient etc.</li>
 * 
 * <li>Whenever the API invokes, send the respective inputs like API name, input
 * requests, request headers, and path parameters to invoke the respective
 * APIs(POST, GET, PATCH, PUT, DELETE).</li>
 * 
 * <li>This interface has a method for POST operation. It can be customised to
 * add other HTTP operations like GET/PATCH/DELETE/PUT also.</li>
 * </ul>
 *
 * @param IRequest               Client specific input, Request object of the
 *                               corresponding payment providers used for
 *                               invoking the API's. Mandatory parameter for
 *                               POST APIs.
 * @param IRequestPathParameters Client specific input, RequestPathParam object
 *                               of the respective payment providers used for
 *                               constructing path parameters. Mandatory
 *                               parameter is based on the payment providers.
 */
public interface IRestHttpClient<IRequest, IRequestPathParameters> {

    /**
     * Method used to invoke POST API needs to implement for the various HTTP
     * clients. This method will create the respective HTTP clients used to invoke
     * the respective APIs after getting the corresponding resources URL, header
     * parameters and input requests.
     *
     * @param apiName           Holds the API name is specific to adapters. Each
     *                          payment provider should maintain their respective
     *                          endpoints for several APIs and use them in getting
     *                          the resources URL.
     * @param requestConfig     Contains the request header configuration values
     *                          mandatory to invoke the API like xapikey for
     *                          authentication etc.
     * @param request           Holds the post body request of the respective
     *                          payment providers and is mandatory for the POST API.
     *                          It is assumed to be JSON content type.
     * @param requestPathParams Contains RequestPathParams object specific to the
     *                          payment gateway, required for constructing the URL
     *                          based on the API endpoints.
     * @return The {@link RestHttpClientResponse} object contains response code and
     *         responses from the invoked POST API.
     */
    public RestHttpClientResponse sendPost(String apiName, Map<String, String> requestConfig, IRequest request,
	    IRequestPathParameters requestPathParams);

}
