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

import java.util.Date;
import lombok.Data;

/**
 * 
 * PaymentCollectionInput class, holds the payment input models.
 * 
 * @author vinay
 */
@Data
public class PaymentCollectionInput extends PaymentCollectionCommon {

    /**
     * Boolean value indicates repeat transactions or not and is mapped to
     * previouslyInvoked.
     */
    @Description("mapped to PreviouslyInvoked")
  private boolean isRepeatTransaction;


  /** Holds the unique reference id for each transactions */
  @Description("Mapped to chargeTransactionKey")
  private String transactionId;

  /** Hold the primary account details. */
  @Description("PAN details")
  private String primaryAccountNumber;

  /** Holds the customer's credit/debit card number. */
  @Description("Credit card/Debit card number")
  private String cardNumber;

  /** Holds the card's expiry date. */
  @Description("Expiry date of the card")
  private Date cardExpiryDate;

  /** Holds the authenticate code of the card. */
  @Description("authenticate code of the card")
  private String secureAuthenticationCode;

  /** Unique identifier for the payment */
  @Description("Unique identifier for the payment")
  private String paymentKey;

  /** Holds the Address line 1 of the billing address. */
  private String billingAddressLine1;

  /** Holds the city of the billing address. */
  private String billingCity;

  /** Holds the country of the billing address. */
  private String billingCountry;

  /** Holds the state of the billing address. */
  private String billingState;

  /** Holds the phone of the billing address. */
  private String billingDayPhone;

  /** Holds the emailid of the billing address. */
  private String billingEmailID;

  /** Holds the zipcoe of the billing address. */
  private String billingZipCode;

  /** Holds the firstname of the billing address. */
  private String billingFirstName;

  /** Holds the middlename of the billing address. */
  private String billingMiddleName;

  /** Holds the lastname of the billing address. */
  private String billingLastName;

  /** Holds the Address line 1 of the shipping address. */
  private String shippingAddressLine1;

  /** Holds the city of the shipping address. */
  private String shippingCity;

  /** Holds the country of the shipping address. */
  private String shippingCountry;

  /** Holds the state of the shipping address. */
  private String shippingState;

  /** Holds the phone of the shipping address. */
  private String shippingDayPhone;

  /** Holds the emailid of the shipping address. */
  private String shippingEmailID;

  /** Holds the zipcode of the shipping address. */
  private String shippingZipCode;

  /** Holds the firstname of the shipping address. */
  private String shippingFirstName;

  /** Holds the middlename of the shipping address. */
  private String shippingMiddleName;

  /** Holds the lastname of the shipping address. */
  private String shippingLastName;
}
