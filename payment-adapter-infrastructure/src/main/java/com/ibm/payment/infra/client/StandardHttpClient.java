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

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ibm.payment.infra.exception.PaymentConnectionException;
import com.ibm.payment.infra.exception.PaymentException;
import com.ibm.payment.infra.requestHandlers.interfaces.RequestHandler;
import com.ibm.payment.infra.response.ExceptionError;
import com.ibm.payment.infra.response.RestHttpClientResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * StandardHttpClient class implement from {@link IRestHttpClient} interfaces,
 * have the implementation of CloseableHttpClient used to invoke API from
 * various payment gateways.
 * 
 * 
 * @param IRequest               Client specific input, Request object of the
 *                               various payment providers.
 * @param IRequestPathParameters Client specific input, RequestPathParam object
 *                               of the various payment providers.
 */
public class StandardHttpClient<IRequest, IRequestPathParameters>
	implements IRestHttpClient<IRequest, IRequestPathParameters> {

    private static final Logger logger = LogManager.getLogger(StandardHttpClient.class);

    private ObjectMapper objectMapper = new ObjectMapper()
	    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
	    .enable(SerializationFeature.INDENT_OUTPUT);

    private RequestHandler<IRequest, IRequestPathParameters, RequestConfig> requestHeaderHandler;

    /**
     * Constructor injects the corresponding payment provider's request handlers.
     * 
     * @param requestHandler Holds {@link RequestHandler} object injects request
     *                       handler specific to payment gateways.
     */
    public StandardHttpClient(RequestHandler<IRequest, IRequestPathParameters, RequestConfig> requestHandler) {
	this.requestHeaderHandler = requestHandler;
    }

    /**
     * <ul>
     * <li>This method is the default implementation of invoking POST API using
     * closeablehttpclient and is created using {@link HttpClientBuilder}. Using
     * {@link HttpPost}, the HTTP client executes to get the payment provider
     * response based on the API name.</li>
     * 
     * <li>Also, Request handlers will construct the resource URL, request headers,
     * and request configs (socket timeout, connection request timeout, connect
     * timeout) used to set timeout configurations based on the implementation of
     * various payment gateways.</li>
     * 
     * <li>Also, throws {@link PaymentConnectionException} when connectivity issues
     * such as Socket Timeout etc. are encountered when connecting to the payment
     * gateway.</li>
     * </ul>
     * 
     * @param apiName           Holds the API name of the adapter used in
     *                          constructing the resources URL.
     * @param requestConfig     Contains the request header configuration values
     *                          that are mandatory to invoke the API like xapikey
     *                          for authentication etc.
     * @param request           Holds the post body request object of the respective
     *                          payment providers and is mandatory for the POST API.
     * @param requestPathParams Holds request path parameters based on the payment
     *                          gateway used for constructing the URL based on the
     *                          API endpoints.
     * @return The {@link RestHttpClientResponse} object contains response code and
     *         responses used to set the HttpClient responses.
     * 
     */
    @Override
    public RestHttpClientResponse sendPost(String apiName, Map<String, String> requestConfig, IRequest request,
	    IRequestPathParameters requestPathParams) {
	logger.debug("StandardHttpClient - sendPost");
	RestHttpClientResponse restHttpClientResponse = new RestHttpClientResponse();

	try (CloseableHttpClient httpclient = HttpClientBuilder.create()
		.setDefaultRequestConfig(requestHeaderHandler.getRequestConfig()).build()) {
	    String url = requestHeaderHandler.getResourceURL(apiName, requestPathParams);
	    logger.debug("Input URL: " + url);
	    HttpPost httppost = new HttpPost(url);

	    Map<String, String> requestHeader = requestHeaderHandler.getHeader(requestConfig, request);

	    requestHeader.forEach((key, value) -> {
		logger.debug("Input Header: " + key + ", Value: " + value);
		httppost.addHeader(key, value);
	    });

	    if (request == null) {
		logger.error("Mandatory Properties Missing - Request is Null ");
		JSONObject errorJSON = new JSONObject();
		errorJSON.put("errorDescription", "Request post object is null");
		throw new PaymentException(ExceptionError.PAY0005, errorJSON);
	    }
	    String content = objectMapper.writeValueAsString(request);
	    StringEntity params = new StringEntity(content);
	    logger.debug("Input POST body content - {}", content);
	    httppost.setEntity(params);

	    try (CloseableHttpResponse httpresponse = httpclient.execute(httppost)) {
		if (httpresponse != null) {
		    restHttpClientResponse.setResponseCode(httpresponse.getStatusLine().getStatusCode());
		    logger.debug("Output ResponseCode - {}", httpresponse.getStatusLine().getStatusCode());
		    if (httpresponse.getEntity() != null) {
			HttpEntity entity = httpresponse.getEntity();
			String response = EntityUtils.toString(entity);
			logger.debug("Response - {}", response);
			restHttpClientResponse.setResponse(response);
		    }
		}
	    }
	} catch (IOException e) {
	    logger.error("StandardHttpClient - sendPost - IOException occurs while making httpclient connection - {}"
		    + Arrays.asList(e.getStackTrace()).stream().map(Objects::toString)
			    .collect(Collectors.joining("\n")));
	    throw new PaymentConnectionException("IOException occurs while making HttpClient connection", e.getCause());
	}
	return restHttpClientResponse;
    }

}
