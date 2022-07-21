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
package com.ibm.payment.infra.requestHandlers.implementation.adyen;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.ibm.model.adyen.AdyenRequest;
import com.ibm.payment.infra.context.AdyenRequestPathParam;
import com.ibm.payment.infra.exception.PaymentConfigurationException;
import com.ibm.payment.infra.exception.PaymentException;
import com.ibm.payment.infra.response.ExceptionError;

import org.apache.http.client.config.RequestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.ibm.adapter.payment.enums.ServiceType;
import com.ibm.payment.infra.requestHandlers.interfaces.RequestHandler;

/**
 *
 * RequestHandlersImpl class implements from {@link RequestHandler} interface,
 * it is specific to adyen payment providers. Configurable values are fetched
 * from properties file based on adyen properties. The methods are listed below
 * with logics <br>
 * <ul>
 * <li>Used for constructing the resource URL based on the apiname. URL and
 * version values are configurable fetched from adyen properties.</li>
 * <li>Used to get request headers values. For Adyen, xapikey is configurable
 * fetched from properties file.</li>
 * <li>Used to get request configuration like socket timeout, connection request
 * timeout, and connect timeout used for configuring the timeout properties in
 * request configs in Httpclient for invoking the corresponding API.</li>
 * </ul>
 *
 * 
 * @param <IRequest>                Indicates the {@link AdyenRequest} object.
 * @param <IRequestHeaderParameter> Holds the {@link AdyenRequestPathParam}
 *                                  object and is mandatory parameter for adyen
 *                                  payment gateways.
 * @param <IRequestConfigs>         Indicates {@link RequestConfig} object used
 *                                  for configuring timeout in HttpClients.
 */
public class RequestHandlersImpl<IRequest, IRequestHeaderParameter, IRequestConfigs>
	implements RequestHandler<IRequest, IRequestHeaderParameter, IRequestConfigs> {

    private static final Logger logger = LogManager.getLogger(RequestHandlersImpl.class);

    private static final String ERROR_DESCRIPTION = "errorDescription";
    private static final String ENDPOINT_CAPTURES = "/captures";
    private static final String ENDPOINT_CANCELS = "/cancels";
    private static final String ENDPOINT_REFUNDS = "/refunds";
    private static final String ENDPOINT_PAYMENTS = "/payments";
    private static final String ENDPOINT_AMEND_AUTHORIZATION = "/amountUpdates";
    private static final String URL = "url";
    private static final String VERSION = "version";
    private static final String X_API_KEY = "xapikey";
    private static final String CONTENT_TYPE = "content-type";
    private static final String CONNECTION_REQUEST_TIMEOUT = "connectionrequesttimeout";
    private static final String CONNECT_TIMEOUT = "connecttimeout";
    private static final String SOCKET_TIMEOUT = "sockettimeout";

    public Properties adyenProperties = new Properties();

    /**
     * Constructor used to fetch the configurable Adyen properties file from the
     * resources folder.
     */
    public RequestHandlersImpl() {
	try (InputStream inputStream = getClass().getResourceAsStream("/adyen.properties")) {
	    adyenProperties.load(inputStream);
	} catch (IOException e) {
	    logger.error(
		    "RequestHandlersImpl - IOException occurs while loading the properties file from resources folder");
	    JSONObject errorJSON = new JSONObject();
	    errorJSON.put(ERROR_DESCRIPTION, "Properties File Not Found");
	    throw new PaymentConfigurationException(ExceptionError.PAY0005, errorJSON);
	} catch (Exception e) {
	    throw new PaymentException(e);
	}
    }

    /**
     * Methods to get the request header parameters are required for Adyen payment
     * gateways to invoke the respective API. Validation happens for mandatory
     * properties like xapikey loads from the Adyen properties file.
     * 
     * @param requestConfig Holds the configuration value used to set the request
     *                      header.
     * @param request       Indicates the Adyen request object.
     * @return The header map values.
     */
    @Override
    public Map<String, String> getHeader(Map<String, String> requestConfig, IRequest request) {
	logger.debug("RequestHandlersImpl - getHeader ");
	if (!adyenProperties.containsKey(X_API_KEY)) {
	    logger.error("Mandatory Properties Missing");
	    JSONObject errorJSON = new JSONObject();
	    errorJSON.put(ERROR_DESCRIPTION, "Mandatory Properties Missing");
	    throw new PaymentConfigurationException(ExceptionError.PAY0005, errorJSON);
	}

	String xapikey = adyenProperties.getProperty(X_API_KEY);
	Map<String, String> headerMap = new HashMap<String, String>();
	headerMap.put("x-api-key", xapikey);
	headerMap.put("content-type", requestConfig.get(CONTENT_TYPE));

	return headerMap;
    }

    /**
     * Method used to construct the resources URL based on the Adyen payment
     * gateways. Validation happen for mandatory properties like URL and version,
     * which are loaded from the Adyen properties file.
     * 
     * @param apiName          Holds API names like payment processing, capture,
     *                         refund, reverse and amend auth.
     * @param requestPathParam Holds request path parameters object.
     * @return The constructed URL is based on the payment providers.
     */
    @Override
    public String getResourceURL(String apiName, IRequestHeaderParameter requestPathParam) {
	logger.debug("RequestHandlersImpl - getResourceURL ");
	if (!(adyenProperties.containsKey(URL) || adyenProperties.containsKey(VERSION) || requestPathParam != null)) {
	    logger.error("Mandatory Properties Missing");
	    JSONObject errorJSON = new JSONObject();
	    errorJSON.put(ERROR_DESCRIPTION, "Mandatory Properties Missing");
	    throw new PaymentConfigurationException(ExceptionError.PAY0005, errorJSON);
	}
	String url = adyenProperties.getProperty(URL);
	String version = adyenProperties.getProperty(VERSION);
	logger.debug(URL + "-" + url + ", " + VERSION + "-" + version);
	AdyenRequestPathParam requestPathParams = (AdyenRequestPathParam) requestPathParam;
	StringBuilder sb = new StringBuilder("");
	if ((ServiceType.PROCESS_PAYMENTS).toString().equals(apiName)) {
	    sb.append(url + "/").append(version).append(ENDPOINT_PAYMENTS);
	} else if ((ServiceType.REVERSE).toString().equals(apiName)) {
	    sb.append(url + "/").append(version).append(ENDPOINT_PAYMENTS + "/")
		    .append(requestPathParams.getPspPaymentReference()).append(ENDPOINT_CANCELS);
	} else if ((ServiceType.CAPTURE).toString().equals(apiName)) {
	    sb.append(url + "/").append(version).append(ENDPOINT_PAYMENTS + "/")
		    .append(requestPathParams.getPspPaymentReference()).append(ENDPOINT_CAPTURES);
	} else if ((ServiceType.AMEND_AUTHORIZATION).toString().equals(apiName)) {
	    sb.append(url + "/").append(version).append(ENDPOINT_PAYMENTS + "/")
		    .append(requestPathParams.getPspPaymentReference()).append(ENDPOINT_AMEND_AUTHORIZATION);
	} else if ((ServiceType.REFUND).toString().equals(apiName)) {
	    sb.append(url + "/").append(version).append(ENDPOINT_PAYMENTS + "/")
		    .append(requestPathParams.getPspPaymentReference()).append(ENDPOINT_REFUNDS);
	}
	logger.debug("getResourceURL - {}", sb.toString());
	return sb.toString();
    }

    /**
     * Method to get and configure the request configurations like socket timeout,
     * connection request timeout, and connect timeout loaded from Adyen properties
     * file. Default values are used for a timeout if it is not configured in the
     * properties file.
     * 
     * @return The RequestConfigs based on the payment gateways.
     */
    @SuppressWarnings("unchecked")
    @Override
    public IRequestConfigs getRequestConfig() {
	logger.debug("RequestHandlersImpl - getRequestConfig ");
	logger.debug(CONNECTION_REQUEST_TIMEOUT + " - " + adyenProperties.getProperty(CONNECTION_REQUEST_TIMEOUT) + ","
		+ CONNECT_TIMEOUT + "-" + adyenProperties.getProperty(CONNECT_TIMEOUT) + ", " + SOCKET_TIMEOUT + "-"
		+ adyenProperties.getProperty(SOCKET_TIMEOUT));
	return (IRequestConfigs) org.apache.http.client.config.RequestConfig.custom()
		.setConnectionRequestTimeout(
			Integer.valueOf(adyenProperties.getProperty(CONNECTION_REQUEST_TIMEOUT, "1000")))
		.setConnectTimeout(Integer.valueOf(adyenProperties.getProperty(CONNECT_TIMEOUT, "1000")))
		.setSocketTimeout(Integer.valueOf(adyenProperties.getProperty(SOCKET_TIMEOUT, "1000"))).build();

    }

}
