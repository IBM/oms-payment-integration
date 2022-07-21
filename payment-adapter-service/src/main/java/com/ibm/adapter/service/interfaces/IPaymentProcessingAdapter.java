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
package com.ibm.adapter.service.interfaces;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ibm.adapter.context.RequestContext;
import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.adapter.util.ServiceUtils;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This interface has methods required which needs to be implemented for the
 * payment adapter. Whenever any modification with price impacts are performed
 * in the OMS , it will translate to one or more payment action.
 * <ul>
 * <li>Perform charge - order/shipment is invoiced and oms wants to charge for
 * the same.</li>
 * <li>Perform cancellation - Entire Order has been cancelled before
 * fulfillment, so oms will cancel the entire authorised amount.</li>
 * <li>Perform refund - order is invoiced and reached its final state.But
 * customer has either returned the item or has been given some appeasement.Oms
 * has to refund money back to customer in this case.</li>
 * <li>Amend authorization - order value has decreased because of some
 * modification.oms has to decrease the authorised amount.</li>
 * <li>Process payments - Oms wants to take a new authorisation because either
 * the old auth has expired or take a new auth for a new charge.</li>
 * </ul>
 *
 * @param <ExternalPaymentInput>  Client specific input, typically
 *                                ExternalPaymentInput
 * @param <ExternalPaymentOutput> Client specific output , typically
 *                                ExternalPaymentOutput
 */
public interface IPaymentProcessingAdapter<ExternalPaymentInput, ExternalPaymentOutput> {
    static final Logger logger = LogManager.getLogger(IPaymentProcessingAdapter.class);
    static ObjectMapper objectMapper = new ObjectMapper()
	    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
	    .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * This method can be leveraged to map oms objects to adapter domain object.
     * Adapter's execute method takes input in the form of PaymentCollectionInput
     * object.Implementors OMS objects will have to adopt to this model inorder for
     * the adapter to work.
     *
     * Implementors can use mapper libraries like mapstruct to map from client
     * specific objects to adapter domain object.
     * 
     * @param requestContext       Context object.
     * @param externalPaymentInput client specific object, typically it will be
     *                             YFSExtnPaymentCollectionInputStruct
     * @return PaymentCollectionInput , this will be the input to the adapter's
     *         execute() method.
     */
    PaymentCollectionInput preProcess(RequestContext requestContext, ExternalPaymentInput externalPaymentInput);

    /**
     * This method will be invoked by adapter to charge against the payment. Charge
     * can be done in two ways :
     * <ul>
     * <li>Full order charge - If charge amount == authorised amount .E.g : Order
     * total is $10 and authorised amount is also $10. If implementor wants to
     * charge for entire $10 , this method should be invoked.</li>
     * <li>Partial charge - If charge amount is less than authorised amount. e.g:
     * Order total is $10 and authorised amount is also $10. Oms wants to charge $5
     * now and want to charge remaining amount later.</li>
     * </ul>
     * 
     * @param requestContext         context object
     * @param paymentCollectionInput adapter's input object
     * @return PaymentCollectionOutput adapter's output object
     */
    PaymentCollectionOutput performCharge(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);

    /**
     * This method will be invoked by the adapter during full order cancellation
     * scenarios. <strong>This method should be used if and only if the order is not
     * charged.Incase if the order is charged, then performRefund sould be used
     * .</strong> <br>
     * e.g: Order was created for a total of $10 and it was cancelled.
     * 
     * @param requestContext         context object
     * @param paymentCollectionInput adapter's input object
     * @return adapter's output object
     */
    PaymentCollectionOutput performReverseAuth(RequestContext requestContext,
                                               PaymentCollectionInput paymentCollectionInput);

    /**
     * This method will be invoked by adapter for all the refund scenarios. <br>
     * <ul>
     * <li>Full refund - Order was created for $100 . Customer returns the entire
     * order.</li>
     * <li>Partial refund - Order was created for $100 , customer wants to return an
     * item worth $40</li>
     * </ul>
     * 
     * @param requestContext         context object
     * @param paymentCollectionInput adapter's input object
     * @return adapter's output object
     */
    PaymentCollectionOutput performRefund(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput);

    /**
     * This method will be used for modifying the existing the authorisation.
     * Whenever a modification has been done on the order involving price change ,
     * oms would either need to take additional auth or decrease existing auth. <br>
     * example scenarios would be :
     * <ul>
     * <li>Partial order cancellation - Order worth $100 was created . Customer
     * cancels items worth $50 . In this case oms might choose to reverse remaining
     * $50.</li>
     * <li>Customer appeasement - Order worth $100 was created. Retailer has
     * provided an appeasement worth $20. In this case retailer does not want to
     * block entire $100 from customer's account.So oms will reverse $20 worth of
     * authorisation.</li>
     * </ul>
     * 
     * @param requestContext         context object
     * @param paymentCollectionInput adapter's input object
     * @return adapter's output object
     */
    PaymentCollectionOutput amendAuthorization(RequestContext requestContext,
                                               PaymentCollectionInput paymentCollectionInput);

    /**
     * This method should be used in following scenarios: <br>
     * <ul>
     * <li>Auth expiry - Existing authorisation has expired and new authorisation
     * has to be generated.</li>
     * <li>Order modification with increase in order totals - Order worth $200 was
     * created. Customer has added additional items to the order and the order total
     * is now $250.Oms creates an additional authorisation request for $50.</li>
     * </ul>
     * 
     * @param requestContext         context object
     * @param paymentCollectionInput adapter's input object
     * @return adapter's output object
     */
    PaymentCollectionOutput processPayment(RequestContext requestContext,
                                           PaymentCollectionInput paymentCollectionInput);

    /**
     * The decision of calling respective payment service interfaces is delegated to
     * adapter.This method decides which interface to call.Based on the adapter's
     * input, this method will <strong>interpret</strong> the oms action and call
     * the required payment service interface. This method has below logic : <br>
     * <ul>
     * <li>Preprocess - convert client specific model to adapters input model</li>
     * <li>Evaluate the input and decide which action to perform.e.g: reverse,
     * amendauth, refund etc.</li>
     * <li>Invoke the methods corresponding to the action. e.g: if action = REVERSE,
     * call performReverse(requestContext,paymentCollectionInput).</li>
     * <li>postprocess - convert adapters output model to client specific model</li>
     * </ul>
     * 
     * @param requestContext       context object
     * @param externalPaymentInput adapter's input object
     * @return adapter's output object
     */
    @SneakyThrows
    default ExternalPaymentOutput execute(RequestContext requestContext, ExternalPaymentInput externalPaymentInput) {
	PaymentCollectionInput paymentCollectionInput = preProcess(requestContext, externalPaymentInput);
	logger.debug("After preprocess >> " + objectMapper.writeValueAsString(paymentCollectionInput));
	// Determine payment service to be invoked
	String action = ServiceUtils.determineAction(paymentCollectionInput);
	logger.debug("Action determined is " + action);
	PaymentCollectionOutput paymentCollectionOutput = null;
	switch (action) {
	case "REVERSE": {
	    paymentCollectionOutput = performReverseAuth(requestContext, paymentCollectionInput);
	    break;
	}
	case "AMEND_AUTHORIZATION": {
	    paymentCollectionOutput = amendAuthorization(requestContext, paymentCollectionInput);
	    break;
	}
	case "CAPTURE": {
	    paymentCollectionOutput = performCharge(requestContext, paymentCollectionInput);
	    break;
	}
	case "REFUND": {
	    paymentCollectionOutput = performRefund(requestContext, paymentCollectionInput);
	    break;
	}
	case "PROCESS_PAYMENTS": {
	    paymentCollectionOutput = processPayment(requestContext, paymentCollectionInput);
	    break;
	}
	}
	return postProcess(requestContext, paymentCollectionOutput);
    }

    /**
     * This method can be leveraged to convert adapter's output to client specific
     * output.Adapter's output will be in the form of PaymentCollectionOutput and
     * the client might have its own object model.This method should be used to map
     * PaymentCollectionOutput to client specific output. <br>
     * Mapping libraries such as mapstruct can be used for this purpose.
     * 
     * @param requestContext context object
     * @param paymentOutput  adapter's output object
     * @return Client specific output, typically
     *         YFSExtnPaymentCollectionOutputStruct
     */
    ExternalPaymentOutput postProcess(RequestContext requestContext, PaymentCollectionOutput paymentOutput);

}
