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
package com.ibm.adapter.sample;

import com.ibm.adapter.context.GenericPaymentServiceInjector;
import com.ibm.adapter.context.PaymentServiceContext;
import com.ibm.adapter.context.RequestContext;
import com.ibm.adapter.enums.PaymentServiceProvider;
import com.ibm.adapter.payment.model.PaymentCollectionInput;
import com.ibm.adapter.payment.model.PaymentCollectionOutput;
import com.ibm.adapter.sample.mapper.PaymentMapper;
import com.ibm.adapter.sample.model.ExternalPaymentInput;
import com.ibm.adapter.sample.model.ExternalPaymentoutput;
import com.ibm.adapter.service.interfaces.IPaymentProcessingAdapter;

import java.util.Objects;

/**
 * Sample implementation for payment adapter
 */
public class AdyenAdapter implements IPaymentProcessingAdapter<ExternalPaymentInput, ExternalPaymentoutput> {
    private static PaymentServiceContext _paymentServiceContext;

    /**
     * Inject the Implementation classes to constructor
     * @param pspType
     */
    private AdyenAdapter(String pspType) {
        if (pspType.contentEquals(PaymentServiceProvider.ADYEN.name()) && Objects.isNull(_paymentServiceContext)) {
            _paymentServiceContext = new GenericPaymentServiceInjector().getServiceContext();
        }
    }

    /**
     * Map client input object to adapter input object
     */
    @Override
    public PaymentCollectionInput preProcess(RequestContext requestContext, ExternalPaymentInput externalPaymentInput) {
        return PaymentMapper.MAPPER.omsToPayment(externalPaymentInput);
    }

    /**
     * Does payment capture
     */
    @Override
    public PaymentCollectionOutput performCharge(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
        return _paymentServiceContext.getCaptureEngine().execute(requestContext, paymentCollectionInput);
    }

    /**
     * This method performs authorization reversal
     */
    @Override
    public PaymentCollectionOutput performReverseAuth(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
        return _paymentServiceContext.getReverseEngine().execute(requestContext, paymentCollectionInput);
    }

    /**
     * Used for refunds . e.g: incase of returns /customer appeasements
     */
    @Override
    public PaymentCollectionOutput performRefund(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
        return _paymentServiceContext.getRefundEngine().execute(requestContext, paymentCollectionInput);
    }

    /**
     * For modifying existing authorizations
     */
    @Override
    public PaymentCollectionOutput amendAuthorization(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
        return _paymentServiceContext.getAmendAuthEngine().execute(requestContext, paymentCollectionInput);
    }

    /**
     * For processing new payment methods or reauthorizations
     */
    @Override
    public PaymentCollectionOutput processPayment(RequestContext requestContext, PaymentCollectionInput paymentCollectionInput) {
        return _paymentServiceContext.getProcessPaymentEngine().execute(requestContext, paymentCollectionInput);
    }

    /**
     * Map adapter output to client object
     */
    @Override
    public ExternalPaymentoutput postProcess(RequestContext requestContext, PaymentCollectionOutput paymentOutput) {
        return PaymentMapper.MAPPER.PaymentToOms(paymentOutput);
    }

    public static IPaymentProcessingAdapter getAdapter(String pspType) {
        return new AdyenAdapter(pspType);
    }
}
