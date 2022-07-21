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
package com.ibm.adapter.util;

import com.ibm.adapter.context.RequestContext;
import com.ibm.adapter.enums.TransactionType;
import com.ibm.adapter.payment.enums.ServiceType;
import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.payment.infra.context.AdyenRequestPathParam;
import com.ibm.payment.infra.exception.PaymentConnectionException;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class provide similar methods used in the service layer. Below are
 * the methods
 * <ul>
 * <li>used to set the request path parameters of PSPReference.</li>
 * <li>determining the actions based on the service types like reversal,
 * capture, refund, and decrease auth. It will validate the transaction types
 * from payment collection input with the service types.</li>
 * <li>handle payment connection exception if connection related issues occurs
 * from the infrastructure layer.</li>
 * <li>handle generic exception to log the other error occurs.</li>
 * </ul>
 */
public class ServiceUtils {

    private static final Logger logger = LogManager.getLogger(ServiceUtils.class);

    /**
     * Method used to set request path parameters, especially PspPaymentReferences
     * in AdyenRequestPathParams is used for constructing the resource URL to invoke
     * the respective payment providers.
     * 
     * @param requestContext         holds request context object used to set Adyen
     *                               request path parameters.
     * @param paymentCollectionInput object used to get authorisation id.
     */
    public static void setRequestPathParams(RequestContext requestContext,
	    PaymentCollectionInput paymentCollectionInput) {
	AdyenRequestPathParam params = new AdyenRequestPathParam();
	params.setPspPaymentReference(paymentCollectionInput.getPaymentReference());
	requestContext.setRequestPathParams(params);
    }

    /**
     * Method used to determine the action based on the service and transaction
     * type. BiPredicate functions validate the service type with the respective
     * transaction type to choose the service type(i.e) Process Payments, Amend
     * Authorisation, Reverse, Refund and Capture.
     * 
     * @param paymentCollectionInput object used to validate the service type and
     *                               request amounts.
     * @return The respective service type is based on the transaction type.
     */
    public static String determineAction(PaymentCollectionInput paymentCollectionInput) {
	BigDecimal openAuthAmount = paymentCollectionInput.getAuthorizedAmount();
	if (isFullReverse.test(paymentCollectionInput, openAuthAmount))
	    return ServiceType.REVERSE.name();
	else if (isDecreaseAuth.test(paymentCollectionInput, openAuthAmount))
	    return ServiceType.AMEND_AUTHORIZATION.name();
	else if (isCapture.test(paymentCollectionInput, openAuthAmount))
	    return ServiceType.CAPTURE.toString();
	else if (isRefund.test(paymentCollectionInput, openAuthAmount))
	    return ServiceType.REFUND.toString();
	else if (isNoAuthIdPresent.test(paymentCollectionInput))
	    return ServiceType.PROCESS_PAYMENTS.toString();
	return "";
    }

    /**
     * This BiPredicate function validates the transaction type and the requested
     * amounts from the payment collection input object for Full cancellation.
     */
    static BiPredicate<PaymentCollectionInput, BigDecimal> isFullReverse = (o, amt) -> {
	return (o.getTransactionType().contentEquals(TransactionType.AUTHORIZATION.name())
		&& (o.getRequestAmount().abs().compareTo(amt) == 0)
		&& o.getRequestAmount().compareTo(BigDecimal.ZERO) < 0);
    };

    /**
     * This BiPredicate function validates the transaction type and the requested
     * amounts from the payment collection input object for Decrease Authorisation.
     * 
     */
    static BiPredicate<PaymentCollectionInput, BigDecimal> isDecreaseAuth = (o, amt) -> {
	return (o.getTransactionType().contentEquals(TransactionType.AUTHORIZATION.name())
		&& o.getRequestAmount().compareTo(amt) != 0 && o.getRequestAmount().compareTo(BigDecimal.ZERO) < 0);
    };

    /**
     * This BiPredicate function validates the transaction type and the requested
     * amounts from the payment collection input object for capture.
     */
    static BiPredicate<PaymentCollectionInput, BigDecimal> isCapture = (o, amt) -> {
	return (o.getTransactionType().contentEquals(TransactionType.CHARGE.name())
		&& o.getRequestAmount().compareTo(BigDecimal.ZERO) > 0);
    };

    /**
     * This BiPredicate function validates the transaction type and the requested
     * amounts from the payment collection input object for refund.
     */
    static BiPredicate<PaymentCollectionInput, BigDecimal> isRefund = (o, amt) -> {
	return (o.getTransactionType().contentEquals(TransactionType.CHARGE.name())
		&& o.getRequestAmount().compareTo(BigDecimal.ZERO) < 0);
    };

    /**
     * This BiPredicate function validated the authorization id from the payment
     * collection input.
     */
    static Predicate<PaymentCollectionInput> isNoAuthIdPresent = (o) -> {
	return Objects.isNull(o.getAuthorizationId()) || o.getAuthorizationId().isEmpty();
    };

    /**
     * Method used to handle the payment connection exception from the service layer
     * and catch it for setting the retry attributes in OMS for future
     * authorisation.
     * 
     * @param paymentCollectionOutput object used to set the retry objects.
     * @param e                       holds payment connection exception which got
     *                                this error if any connection-related errors.
     * @return The PaymentCollection output object.
     */
    public static PaymentCollectionOutput handlePaymentConnectionException(
	    PaymentCollectionOutput paymentCollectionOutput, PaymentConnectionException e) {
	logger.error("PaymentConnectionException Occurs, setting field for retry"
		+ Arrays.asList(e.getStackTrace()).stream().map(Objects::toString).collect(Collectors.joining("\n")));
	logger.error("PaymentConnectionException Occurs, setting field for retry");
	paymentCollectionOutput.setRetry("Y");
	return paymentCollectionOutput;
    }

    /**
     * Method used to handle the generic exception from the service layer.
     * 
     * @param e holds Exception class if any generic error occurs from the service
     *          layer.
     */
    @SneakyThrows
    public static void handleGenericException(Exception e) {
	logger.error("Exception Occurs while calling Payment service provider.Please investigate this exception!"
		+ Arrays.asList(e.getStackTrace()).stream().map(Objects::toString).collect(Collectors.joining("\n")));
	throw e;
    }
}
