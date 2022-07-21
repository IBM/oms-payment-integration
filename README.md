<h1>OMS payment adapter</h1>

This is a java based library that can be used to connect Order management system with Payment service providers(PSP). This version of the adapter supports integration with Adyen.

1. [Objectives](#Objectives)
2. [Overview](#overview)
    - [Network Tokenization](#network-tokenization)
    - [Payment transaction](#payment-transaction)
3. [Getting Started](#getting-started)
4. [Directory structure](#directory-structure)
5. [Integrations required for complete payment processing ](#integrations-required-for-complete-payment-processing)
6. [Architecture](#architecture)
7. [Tutorial](#tutorial)
8. [Supported Payment service providers](#supported-payment-service-providers)
9. [Supported Payment Types](#supported-payment-types)
10. [Sample Payloads](#sample-payloads)
11. [Frequently asked questions](#frequently-asked-questions)
12. [Further reading](#further-reading)
13. [List of dependencies](#list-of-dependencies)


## Objectives

The objectives of the oms-payment adapter are:

- To abstract the details of data mapping and connection from the order management system to the payment gateway so that implementors can focus mainly on building the business logic.
- To provide a reusable and extensible framework that can be extended by implementors.
- A framework that is independent of any order management system.

## Overview

Any merchant who runs an online or an e-commerce business, irrespective of the industry or vertical, is required to support multiple payment methods such as credit cards, debit cards, store value cards and increasingly, digital payments such as Apple Pay or Google Pay. This requires the order management system to be integrated with the payment service providers. Considering the complexities in these integrations and the multiple scenarios that are possible, it is recommended to use a generic adapter that abstracts the complexities of integration with payment gateways .

The adapter is designed to abstract the complexities related to the integration with the payment gateway and enable integration with one or more payment gateways. It also helps simplify the onboarding of new payment gateways. The adapter supports both, the check-out and the order fulfillment use cases.

Payment service providers (PSPs) manage the payment lifecycle for online businesses that need to accept online payments These PSPs enable businesses to accept payments through e-commerce, mobile, and point of sale systems. Any order management system deployment has various touchpoints from where it needs to call PSP during authorization, charge, or refunds. PSPs use their tokenization along with network tokenization that is provided by card networks, thereby significantly reducing the exposure to PCI DSS compliance requirements. For example, an order is created through  an order management system or any other way and it contains all the payment information.  To  process the payment, you must have a layer between the order management system and the payment gateway, which handles all the payment information . This adapter layer simplifies all these logic and mapping and provides a smooth payment transaction experience.

## Network tokenization 

Card networks provide a service that creates a network token for a card. This network token is stored by the payment processor and can be used in place of the card during the authorisation. In this case, you can continue using your regular payments flow and the payment processor takes care of swapping card details automatically for a network token when necessary.

## What is a Payment transaction?
Execution of any order that is initiated by the paying party, the beneficiary, the person authorized to issue official transfer orders or the issuer of the summons for transfer, following a certain method of payment, irrespective of the legal relationship between the paying party and the beneficiary.

## Getting Started

<h3>Features supported by the adapter</h3>

This adapter can integrate your order management system with PSPs and currently supports the following functionalities:

<ol>
	<li>
Capture payment: For charging a credit card.
</li>
	<li>
Reverse authorization: For reversing an existing authorization completely.
</li>
	<li>
Amend authorization: - For increasing or decreasing an existing authorization. This is different than reverse authorization. For example, An order has $100 authorization. Then, based on the scenario, different ways of authorization reversal can be used. 

<ol>
			<li>
Order is completely canceled : Use reverse authorization

</li>
			<li>
Order is modified and there is an increase or decrease in price - Use amend authorization. </li>
		</ol>
		<li>
Refund processing
</li>
		<li>
Re-authorization of a credit card: If the authorisation is expired, a new authorization must be generated. 
</li>
	</ol>

<img src="images/adapter-generic.png" width="800" height="500">

See 1.a from the diagram. This adapter calls  Adyen for all the above-mentioned scenarios in an asynchronous manner. For every call that is made to Adyen,- it responds with a pspReference (unique ID for this request) to acknowledge the request. After processing the request, Adyen notifies the successful or failed status of the request via webhook. pspReference is required to uniquely relate the response with the request made from the order management system.

<h3>Features not supported by the adapter</h3>

<ol>
<li>
The notifications from Adyen must be processed to update the successful or failed status of the transaction. This is not supported by the adapter. See  2.a in the diagram. 
</li>
<li>
The adapter does not capture payment information at the time of purchase. You must have integration already set up to capture payment details and then send the details to your order management system during the order creation process. 
</li>
</ol>

<h3>Who should be using this adapter</h3>

You can use this adapter for the following scenarios or if you meet the following conditions:

<ol>
<li>You use  an order management system or a payments microservice and you need to process payments.</li>
<li>You want to integrate with one or more PSPs.</li>
<li>You have more than one order management system such as a legacy one) along with the modern ERP solution. For example, IBM Sterling or a payment microservice and you want to maintain consistent business logic between the applications.</li>
<li>Manage a code between your order management system and the PSP with a separation of business logic and integration logic or you want to migrate from one PSP to another with minimum changes.</li>
</ol>

<h3>Prerequisites:</h3>

<ol>
<li>

Adyen account: To connect with Adyen, you must [create a test account](https://docs.adyen.com/get-started-with-adyen#test-account)
with Adyen and also [generate an API key](https://docs.adyen.com/development-resources/api-credentials#generate-api-key)
</li>
<li>

An order management system or a payment microservice: An ERP like [IBM Sterling Order Management ](https://www.ibm.com/products/order-management)
or a payment microservice needs a PSP for payment processing.
</li>
<li>

Payment operations such as capture, authorization, and refunds are  against an existing order, which would already have payment details provided by the customer. The customer typically provides payment details through an order capture channel such an e-commerce website, call center applications, POS, or mobile. If this integration is not present at the moment, you can use the 
[pay by link](https://www.adyen.com/pay-by-link) option to capture payment details or create a new integration by using the [dropin integration](https://docs.adyen.com/online-payments/web-drop-in).
</li>

<li>
IDE - An IDE, preferably Intellij, is required. 
</li>
</ol>

## Directory structure

````
├── adapter-build
├── adyen-clients
├── payment-adapter-infrastructure
├── payment-adapter-model
├── payment-adapter-sample-code
└── payment-adapter-service
````

<ol>

<li>
<h3>adapter-build</h3>  This directory contains the 'adapter-build-1.0-SNAPSHOT.jar' jar which gets built after the maven packaging.
</li>

<li>

<h3>adyen-clients</h3>  This directory contains the request and response JSON schemas of Adyen. If you need to update the request and response attributes, these schema files must be updated. The [org.jsonschema2pojo](https://www.jsonschema2pojo.org/) maven plugin is used to generate POJOs when this module is compiled.

````
.
├── pom.xml
├── src
│   └── main
│       └── resources
│           └── schema
│               ├── AdyenCommon.json
│               ├── AdyenRequest.json
│               └── AdyenResponse.json
````

</li>

<li>
<h3>payment-adapter-infrastructure</h3> This directory contains all the files that are related to infrastructure layer, as described in the architecture section. It contains the code that is related to HTTP clients, exception handling, request and response handlers, and mappers. 

````
.
├── AdyenToPaymentResponseMapper.java
└── PaymentToAdyenRequestMapper.java
````
The mappers are very important and this directory contains the code to map adapters object model to the Adyen object model, and vice versa.
This mapping is done by using the [mapstruct library](https://mapstruct.org/).

</li>

<li>
<h3>payment-adapter-model</h3> This directory contains the object definitions of the adapter. As a consumer of adapter, it is very important to understand the input and output attributes that are expected by the adapter and also about how to map order management system input/output to the  input/output of the adapter. 

````
.
├── PaymentCollectionCommon.java
├── PaymentCollectionInput.java
└── PaymentCollectionOutput.java
````
**PaymentCollectionCommon** is an abstract class and contains common attributes that are required by both, **PaymentCollectionInput** and **PaymentCollectionOutput**.

Go through the Javadoc or the annotation **@description** under each attribute to learn more about the attributes. 
Some of the attributes are mandatory for the adapter to function. The rest of the attributes are added for future use cases.
</li>

<li>
<h3>payment-adapter-sample-code</h3> This directory contains the sample payment adapter implementation, mappers to map. an example client model for the adapter object model, and vice versa. More details about the sample are explained in the later part of readme file. 
</li>

<li><h3>payment-adapter-service</h3>  This directory contains the code that corresponds to the service layer. This layer contains the code that is not specific to a PSP.</li>
</ol>

## Integrations required for complete payment processing 

Payment processing is one of the components in a larger ecosystem that consist of multiple moving parts. This adapter bridges your order management system with a payment processor and removes complexities around the payment processing. Since there are multiple participants such as e-commerce, order management system, and payment processor, there must be at least four integrations to execute end-to-end flows by using the adapter.

<img src="images/integrations.png" width="800" height="800">

This adapter helps you with the implementation of the integration.

The tutorial section explains in detail about the implementation. The rest of the integrations are out of scope but reference links are added in the "Further reading" section.

<ol>
<li>

<h4>Checkout integration:</h4>You must integrate the check-out process to create a shopping cart and make an online payment. Adyen provides multiple options line drop-ins, APIs, plug-ins, and pay-by links. The easiest way to check out is to create a payment link After the online payment is done, the credit card becomes authorized and a unique reference id (pspreference) is generated. This ID must be passed on to the order management system during the order creation.
</li>

<li>

<h4>Create order integration:</h4>This is an integration between your E-commerce UI (any order capturing channel) and the order management system. This integration passes on the pspreference and other payment details to the order management system.
</li>

<li>

<h4>OMS to adapter integration:</h4> This is where the order management system-payment adapter comes in to picture. The entire tutorial section explains about this integration. The scope of the adapter is limited to this integration only.
</li>

<li>

<h4>Adyen to OMS integration:</h4> For each call that is made to Adyen by using the integration, Adyen notifies the status of each transaction through a webhook. So, an integration must be in place to consume these updates and then update the status of each transaction.

</li>
</ol>

## Architecture

<img src="images/Adapter.png" width="800" height="500">

There are 2 layers in the oms-payment adapter:
<ol>
<li>

<h3> Service Layer</h3>
Service Layer provides a set of services for each API or transaction that is provided by the payment service provider (payment gateway). These services can be started from the Client, which can be an order management system UE Implementation, an E-commerce check out UI, or the Call Center or Store UI. Service Layer also contains business logic that is related to individual operations.

-	The service layer calls infra layer methods to perform various payment transactions and hold the logic for these transactions.
-	This layer also contains various injectors and contexts.
-	It does eligibility evaluation by using request context and determines the actions to be performed.
-	It also handles exceptions- whenever required such as connectivity exception and sets the retry flag. Some exceptions that come from the payment processor are routed to the user exit (UE).

</li>
<li>

<h3> Infrastructure Layer</h3>
The infrastructure layer acts as a bridge between the service layer and the  external payment service providers. This layer contains the logic to transform the generic model to a Payment Service Provider-specific model such as Adyen. 

- It holds the logic for calling payment processor service invocation and validation.
- It generates all the custom exceptions that are related to the mapper, connection, and configuration. It also handles HTTP exceptions and routes them to the service layer.
- It handles the logic for forward and reverse mapping. All payment processor-specific mapping logic is implemented by using the mapping libraries such as MapStruct.
- It constructs actual request URL, headers, and payload.
- It contains the logic for handling response that is specific to payment processor and maps the response back to the adapter's model.

</li>
</ol>

## Tutorial

A brief tutorial on how to use this adapter can be found [here](Tutorial.md).

First-time users are recommended to go through the **Getting Started** section before going through the tutorial.

## Supported payment service providers

<ol>
<li>
Adyen:The payment adapter is currently designed for Adyen integration.
</li>

**Other PSP's can be integrated by rewriting the infrastructure layer specific to that PSP.**
</ol>

## Supported Payment Types
<ol>
<li>Standalone credit cards</li>
<li>Credit card within Apple Pay wallet</li>
</ol>

## Sample payloads

Examples for each payment action such as capture, authorization, and reauthorization can be  found [here](SamplePayloads.md).

## Frequently asked questions

<ol>
<li>
Since this is an asynchronous payment processing, an additional integration with process notifications is required. Could we not have just avoided this additional integration?
<br>
Answer - The additional integration for processing notifications is required because Adyen currently does not support synchronous processing. But asynchronous processing provides significant performance benefits and backpressure handling. 

</li>
<li>
Instead of making asynchronous calls to PSP, can we make synchronous calls too?
<br>
Answer - Adyen currently does not support synchronous . So you cannot make synchronous calls with the adapter. In future, if any other PSP supports synchronous calls, then that feature might be added to the adapter.
</li>
<li>
How does the adapter figure out that which service (capture, authorization, or reauthorization) to call? 
<br>
Answer - It depends on the attributes that are passed in the PaymentCollectionInput. Depending on the transactionType, requestAmount, and authorizedAmount, the adapter resolves the service to be started. For example, if transactionType='CHARGE' and requestAmount is positive, then the adapter calls the capture service.

</li>
</ol>

## Further reading

| Topic                     	| Resources                              	|
|---------------------------	|----------------------------------------	|
| Checkout integration      	| https://docs.adyen.com/online-payments 	|



## List of dependencies

| Name                      	| Comments                                                 	|
|---------------------------	|----------------------------------------------------------	|
| jackson-core              	|                                                          	|
| jackson-annotations       	|                                                          	|
| jackson-databind          	|                                                          	|
| lombok                    	| To reduce boiler plate code wherever possible            	|
| javax.annotation-api      	|                                                          	|
| junit-jupiter-api         	| For unit test cases                                      	|
| mapstruct                 	| For mapping client objects to adapters object model      	|
| jsonschema2pojo           	| To enforce schema on Adyens request and response formats 	|
| org.apache.httpcomponents 	|                                                          	|
| Mockwebserver             	| To mock Adyen responses during testing                   	|
| org.json                  	|                                                          	|


