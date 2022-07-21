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
package com.ibm.adapter.payment.model;

import com.ibm.adapter.payment.interfaces.Description;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
abstract class PaymentCollectionCommon {

    /** Indicates the unique reference of the payment transactions. */
    @Description("Unique reference of the payment transactions.")
    protected String authorizationId;

    /** Holds the Type of transaction */
    @Description("Holds the Type of transaction.")
    public String transactionType;

    /**
     * Indicates the uniqueID for the merchant as registered in the payment service
     * provider.
     */
    @Description("Indicates the uniqueID for the merchant as registered in the payment service provider.")
    private String merchantId;

    /** It indicates the amount requested for auth or charging. */
    @Description("It indicates the amount requested for auth or charging.")
    private BigDecimal requestAmount;

    /** Indicates the authorized amount for auth or charging. */
    @Description("Indicates the authorized amount for auth or charging")
    private BigDecimal authorizedAmount;

    /** Indicates the Currency code. */
    @Description("Indicates the currency code.")
    private String Currency;

    /** It describes the type of payment. */
    @Description("It describes the type of payment.")
    private String paymentType;

    /** Indicates the date of auth expiration. */
    @Description("Indicates the date of auth expiration.")
    private Date authExpiryDate;

    /** Indicates the last 4 digits of Credit card. */
    @Description("Indicates the last 4 digits of Credit card number.")
    private String displayCCNumber;

    /** It refers the unique identifier for payment. */
    @Description("It refers the unique identifier for payment.")
    private String paymentReference;

    /**
     * Incase of return orders, PSP needs to know the paymentReference against which
     * refund has to be processed.
     */
    @Description("Incase of return orders, PSP needs to know the paymentReference against which refund has to be processed.")
    private String refundPaymentReference;

    /** Refers to the unique identifier for capture. */
    @Description("Refers to the unique identifier for capture.")
    private String captureReference;

    /** Refers to the unique identifier for reverse auth. */
    @Description("Refers to the unique identifier for reverse auth.")
    private String reverseReference;

    /** Holds reference attribute to store PSP specific attribute. */
    @Description("Holds reference attribute to store PSP specific attribute.")
    private String paymentReference1;

    /** Indicates reference attribute to store PSP specific attribute. */
    @Description("Indicates reference attribute to store PSP specific attribute.")
    private String paymentReference2;

    /** Holds Reference attribute to store PSP specific attribute. */
    @Description("Holds Reference attribute to store PSP specific attribute.")
    private String paymentReference3;

    /** Holds unique identification to the order. */
    @Description("Holds unique identification to the order.")
    private String orderNo;

}
