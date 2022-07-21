<h1>Sample Payloads</h1>

This section documents the inputs and outputs expected by the adapter for each of its services(charge, auth, etc).For each of the requests,
PaymentCollectionInput will have an attribute called **asyncReqId** . Whenever Adyen sends the status notification via webhook, it will bear the **pspReference** which
will be the same as **asyncReqId**. It should be used to correlate the request and response.


<ol>
<li>
Reverse Auth: If OMS wants to reverse the entire requested amount and transactionType="AUTHORIZATION".

- PaymentCollectionInput:

````json
{
  "authorizationId" : "AUTH_ID_SAMPLE",
  "transactionType" : "AUTHORIZATION",
  "merchantId" : "IBMAccount480ECOM",
  "requestAmount" : -400.0,
  "authorizedAmount" : 400.0,
  "paymentType" : "CREDIT_CARD",
  "paymentReference2" : "customer12345",
  "paymentReference3" : "TFZH94H2Q6TG5S82",
  "orderNo" : "Y100003200",
  "currency" : "USD"
}
````

- PaymentCollectionOutput:
````json
  {

  "isAsyncReq":true,
  "asyncReqId":"PSP12345",
  "paymentReference","PAYMENT_REF001"
  }
 ````
</li>

<li>
Decrease authorization: The requestAmount is negative and transactionType="AUTHORIZATION"

- PaymentCollectionInput:

````json
{
  "authorizationId" : "AUTH_ID_SAMPLE",
  "transactionType" : "AUTHORIZATION",
  "merchantId" : "IBMAccount480ECOM",
  "requestAmount" : -100.0,
  "authorizedAmount" : 400.0,
  "paymentType" : "CREDIT_CARD",
  "paymentReference2" : "customer12345",
  "paymentReference3" : "TFZH94H2Q6TG5S82",
  "orderNo" : "Y100003200",
  "currency" : "USD"
}
````

- PaymentCollectionOutput:
````json
   {

  "isAsyncReq":true,
  "asyncReqId":"PSP12345",
  "paymentReference","PAYMENT_REF001"
}
 ````
</li>


<li>
capture: The requestAmount will be at max equal to authorized amount and transactionType="CHARGE"

- PaymentCollectionInput:

````json
 {
  "authorizationId" : "AUTH_ID_SAMPLE",
  "transactionType" : "CHARGE",
  "merchantId" : "IBMAccount480ECOM",
  "requestAmount" : 100,
  "authorizedAmount" : 100,
  "paymentType" : "CREDIT_CARD",
  "paymentReference2" : "customer12345",
  "paymentReference3" : "TFZH94H2Q6TG5S82",
  "orderNo" : "Y100003200",
  "currency" : "USD"
}
````

- PaymentCollectionOutput:
````json
   {

  "isAsyncReq":true,
  "asyncReqId":"PSP12345",
  "paymentReference","PAYMENT_REF001"
}
 ````
</li>

<li>
Refund: The requestAmount will be negative and transactionType="CHARGE". 

- PaymentCollectionInput:

````json
  {
  "authorizationId" : "AUTH_ID_SAMPLE",
  "transactionType" : "CHARGE",
  "merchantId" : "IBMAccount480ECOM",
  "requestAmount" : -10,
  "authorizedAmount" : 100,
  "paymentType" : "CREDIT_CARD",
  "paymentReference2" : "customer12345",
  "paymentReference3" : "TFZH94H2Q6TG5S82",
  "orderNo" : "Y100003200",
  "currency" : "USD"
}
````

- PaymentCollectionOutput:
````json
   {

  "isAsyncReq":true,
  "asyncReqId":"PSP12345",
  "paymentReference","PAYMENT_REF001"
}
 ````
</li>

</ol>