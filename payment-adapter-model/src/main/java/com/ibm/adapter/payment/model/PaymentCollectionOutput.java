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
import lombok.Data;

/**
 * 
 * PaymentCollectionOutput class, holds the payment output models.
 * 
 * @author pramodp
 */
@Data
public class PaymentCollectionOutput extends PaymentCollectionCommon{

    /** Describes if this is a sync or async call. */
  @Description("Describes if this is a sync or async call")
    private boolean isAsyncReq;

  /**
   * Unique identifier to for the async request. This value will be updated during
   * recordExternalCharges async.
   */
  @Description("Unique identifier to for the async request.This value will be updated during recordExternalCharges async.")
  private String asyncReqId;

  /** Holds the date of payment capture */
  @Description("Holds the date of the payment capture")
  private String captureDate;

  /** Describes if its a failed payment */
  @Description("Describes if its a failed payment")
  private boolean isFailedPayment;

  /** Describes if autorisation call is required */
  @Description("Describes if autorisation call is required")
  private String requiresAuthCall;

  /** Describes if this request should be retried */
  @Description("Describes if this request should be retried")
  private String retry;
}
