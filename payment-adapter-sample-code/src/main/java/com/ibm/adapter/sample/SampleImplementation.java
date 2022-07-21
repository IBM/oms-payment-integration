package com.ibm.adapter.sample;

import com.ibm.adapter.context.RequestContext;
import com.ibm.adapter.enums.PaymentServiceProvider;
import com.ibm.adapter.sample.mock.MockHelper;
import com.ibm.adapter.sample.model.ExternalPaymentInput;
import com.ibm.adapter.sample.model.ExternalPaymentoutput;
import com.ibm.adapter.service.interfaces.IPaymentProcessingAdapter;

/**
 *This class demonstrates how to invoke oms-payment adapter
 */
public class SampleImplementation {
    /**
     * Initialize adyen adapter
     */
    IPaymentProcessingAdapter adyenAdapter=AdyenAdapter.getAdapter(PaymentServiceProvider.ADYEN.name());


    /**
     * Convert to adapters input and invoke adapter
     * @param externalPaymentInput
     * @return
     */
    ExternalPaymentoutput collectionCreditCard(ExternalPaymentInput externalPaymentInput){

    //Use RequestContext to pass context data to adapter if required.
    //It can also be used to override business logic to skip capture,auth,refund calls
    RequestContext ctx=MockHelper.getRequestContext();

    //Invoke call adapter
    ExternalPaymentoutput externalPaymentoutput= (ExternalPaymentoutput) adyenAdapter.execute(ctx,externalPaymentInput);

    return externalPaymentoutput;
    }

}
