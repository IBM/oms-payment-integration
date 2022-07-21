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
package com.ibm.payment.infra.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.ibm.model.adyen.AdyenResponse;
import com.ibm.payment.infra.exception.PaymentException;
import com.ibm.payment.infra.exception.PaymentMappingException;
import com.ibm.payment.infra.response.ExceptionError;
import com.ibm.payment.infra.response.RestHttpClientResponse;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Utility class which stores reusable code.
 * 
 */
public class InfraServiceUtils {

    private static final Logger logger = LogManager.getLogger(InfraServiceUtils.class);

    /**
     * Method is used to get response based on the restHttpClientResponse.
     * 
     * @param restHttpClientResponse Holds {@link RestHttpClientResponse} response
     *                               object which contains response code and
     *                               response from the StandardHttpClient.
     * @return The {@link AdyenResponse} object.
     */
    public static AdyenResponse getResponse(RestHttpClientResponse restHttpClientResponse) {
	logger.debug("InfraServiceUtils - getResponse");
	AdyenResponse adyenResponse = null;
	int httpResponseCode = restHttpClientResponse.getResponseCode();
	String response = restHttpClientResponse.getResponse();
	logger.debug("ResponseCode - {}", httpResponseCode);
	try {
	    if (isGoodClientResponse(restHttpClientResponse)) {
		if (response != null) {
		    adyenResponse = new AdyenResponse();
		    ObjectMapper objectMapper = new ObjectMapper();
		    adyenResponse = objectMapper.readValue(response, AdyenResponse.class);
		}
	    } else {
		switch (httpResponseCode) {
		case HttpStatus.SC_BAD_REQUEST: {
		    throw new PaymentException(ExceptionError.PAY0001, new JSONObject(response));
		}
		case HttpStatus.SC_UNAUTHORIZED:
		case HttpStatus.SC_FORBIDDEN: {
		    throw new PaymentException(ExceptionError.PAY0002,
			    new JSONObject().put("ErrorMessage", "Unauthorised"));
		}
		case HttpStatus.SC_NOT_FOUND: {
		    throw new PaymentException(ExceptionError.PAY0003, new JSONObject(response));
		}
		case HttpStatus.SC_INTERNAL_SERVER_ERROR:
		case HttpStatus.SC_NOT_IMPLEMENTED:
		case HttpStatus.SC_SERVICE_UNAVAILABLE: {
		    throw new PaymentException(ExceptionError.PAY0004, new JSONObject(response));
		}
		default: {
		    throw new PaymentException(ExceptionError.PAY0007, new JSONObject(response));
		}
		}
	    }

	} catch (JsonProcessingException e) {
	    logger.error(
		    "InfraServiceUtils - getResponse JsonProcessingException occurs while parsing the response.Please investigate this exception!"
			    + Arrays.asList(e.getStackTrace()).stream().map(Objects::toString)
				    .collect(Collectors.joining("\n")));
	    throw new PaymentMappingException(ExceptionError.PAY0006, new JSONObject(response));
	}
	return adyenResponse;
    }

    /**
     * This Predicate used to validate HTTP response code for 200, 201 and 202.
     */
    public static final Predicate<RestHttpClientResponse> isGoodResponse = o -> {
	int httpResponseCode = o.getResponseCode();
	return ((httpResponseCode == HttpStatus.SC_ACCEPTED) || (httpResponseCode == HttpStatus.SC_OK)
		|| (httpResponseCode == HttpStatus.SC_CREATED));
    };

    /**
     * Method used to check the RestHttpClientResponse's as 200 response.
     * 
     * @param restHttpClientResponse holds {@link RestHttpClientResponse} object
     *                               used to check the responsecode.
     * @return The boolean value indicates whether it is eligible for a good
     *         response.
     */
    public static boolean isGoodClientResponse(RestHttpClientResponse restHttpClientResponse) {
	return isGoodResponse.test(restHttpClientResponse);
    }

}
